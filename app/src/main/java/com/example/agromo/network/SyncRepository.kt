package com.example.agromo.network

import android.content.Context
import android.widget.Toast
import com.example.agromo.data.FormularioDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object SyncRepository {

    private const val API_URL = "https://ekgcss8ww8o4ok480g08soo4.91.98.193.75.sslip.io/api/agromo/forms/1/submission"

    // ✅ obtiene el token real guardado en SessionManager
    private fun getAuthHeader(context: Context): String {
        val sessionManager = SessionManager(context)
        val token = sessionManager.getToken()
        return if (!token.isNullOrEmpty()) "Bearer $token" else ""
    }

    private const val API_KEY = "agromo-key-123"

    private val client = OkHttpClient()

    // dentro de object SyncRepository
    suspend fun syncPendingFormularios(context: Context, formularioDao: FormularioDao) {
        withContext(Dispatchers.IO) {
            val pendientes = formularioDao.getPendingFormularios()

            if (pendientes.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No hay formularios pendientes por sincronizar", Toast.LENGTH_SHORT).show()
                }
                return@withContext
            }

            // ✅ Notificar inicio
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Iniciando sincronización...", Toast.LENGTH_SHORT).show()
            }

            for (form in pendientes) {
                val imageFile = File(form.imagenUri)
                if (!imageFile.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Imagen no encontrada para ${form.id}", Toast.LENGTH_SHORT).show()
                    }
                    continue
                }

                try {
                    val requestBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "image",
                            imageFile.name,
                            imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        )
                        .addFormDataPart(
                            "metadata",
                            """
                        {
                            "cultivo": "${form.cultivo}",
                            "fecha_siembra": "${form.fecha_siembra}",
                            "humedad": ${form.humedad.toDoubleOrNull() ?: 0},
                            "metodo_humedad": "${form.metodo_humedad}",
                            "ph": ${form.ph.toDoubleOrNull() ?: 0},
                            "metodo_ph": "${form.metodo_ph}",
                            "altura_planta": ${form.altura_planta.toDoubleOrNull() ?: 0},
                            "metodo_altura": "${form.metodo_altura}",
                            "estado_fenologico": "${form.estado_fenologico}",
                            "densidad_follaje": "${form.densidad_follaje}",
                            "color_follaje": "${form.color_follaje}",
                            "estado_follaje": "${form.estado_follaje}",
                            "observaciones": "${form.observaciones}",
                            "localizacion": "${form.localizacion}"
                        }
                        """.trimIndent()
                        )
                        .build()

                    val request = Request.Builder()
                        .url(API_URL)
                        .addHeader("Authorization", getAuthHeader(context))
                        .addHeader("apikey", API_KEY)
                        .post(requestBody)
                        .build()

                    val response = client.newCall(request).execute()

                    // ✅ Validar response ANTES de cambiar estado
                    if (response.isSuccessful) {
                        formularioDao.markFormularioAsSynced(form.id)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Sincronización Formulario ${form.id} completada", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // ❌ Si la API responde error → detener todo
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Hubo error al sincronizar Formulario ${form.id}", Toast.LENGTH_SHORT).show()
                        }
                        break // 🔥 detiene el proceso aquí
                    }

                } catch (e: Exception) {
                    // ❌ Error de conexión o sin respuesta del servidor
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Hubo error al sincronizar Formulario ${form.id}", Toast.LENGTH_SHORT).show()
                    }
                    break // 🔥 detener el proceso completo
                }
            }
        }
    }
}
