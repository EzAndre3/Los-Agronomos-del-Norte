package com.example.agromo.weather

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class WeatherInfo(
    val temperature: Double,
    val description: String
)

// Modelos para parsear la respuesta
data class OpenMeteoResponse(
    val current_weather: CurrentWeather
)

data class CurrentWeather(
    val temperature: Double,
    val weathercode: Int
)

// Instancias reutilizables para mejor rendimiento
private val client = OkHttpClient()
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()) // Corrección: Añadido el adaptador para Kotlin
    .build()

// Convertida a función de suspensión
suspend fun getWeather(latitude: Double, longitude: Double): WeatherInfo? {
    val url = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current_weather=true"
    val request = Request.Builder().url(url).build()

    // Usamos una extensión para convertir la llamada asíncrona de OkHttp a una suspend function
    val response = client.newCall(request).await()

    if (!response.isSuccessful) {
        response.close()
        return null
    }

    val body = response.body?.string()
    if (body == null) {
        response.close()
        return null
    }

    return try {
        val adapter = moshi.adapter(OpenMeteoResponse::class.java)
        val weatherResponse = adapter.fromJson(body)
        if (weatherResponse != null) {
            WeatherInfo(
                temperature = weatherResponse.current_weather.temperature,
                description = weatherCodeToDescription(weatherResponse.current_weather.weathercode) // Lógica de descripción
            )
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        response.close()
    }
}

// Función auxiliar para convertir la llamada de OkHttp en suspend
private suspend fun Call.await(): Response {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response)
            }
            override fun onFailure(call: Call, e: IOException) {
                if (continuation.isCancelled) return
                continuation.resumeWithException(e)
            }
        })
        continuation.invokeOnCancellation {
            cancel()
        }
    }
}

// Función para mapear el código del clima
private fun weatherCodeToDescription(code: Int): String {
    return when (code) {
        0 -> "Cielo despejado"
        1 -> "Principalmente despejado"
        2 -> "Parcialmente nublado"
        3 -> "Nublado"
        45 -> "Niebla"
        48 -> "Niebla con escarcha"
        51 -> "Llovizna ligera"
        53 -> "Llovizna moderada"
        55 -> "Llovizna densa"
        61 -> "Lluvia ligera"
        63 -> "Lluvia moderada"
        65 -> "Lluvia fuerte"
        80 -> "Chubascos ligeros"
        81 -> "Chubascos moderados"
        82 -> "Chubascos violentos"
        95 -> "Tormenta"
        else -> "Desconocido"
    }
}
