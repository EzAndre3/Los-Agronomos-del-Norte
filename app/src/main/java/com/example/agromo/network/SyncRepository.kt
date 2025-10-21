package com.example.agromo.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.agromo.data.FormularioEntity
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

    suspend fun syncFormularios(context: Context, formularios: List<FormularioEntity>) {
        withContext(Dispatchers.IO) {
            try {
                for (form in formularios) {
                    val imageFile = File(form.imagenUri)
                    if (!imageFile.exists()) continue

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
                    Log.d("SYNC", "Formulario ${form.id} → ${response.code}: ${response.message}")
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Sincronización completada", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("SYNC", "Error al sincronizar formularios", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error durante la sincronización", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
