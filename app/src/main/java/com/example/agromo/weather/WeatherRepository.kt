package com.example.agromo.weather

import android.content.Context
import android.location.Geocoder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Contiene todos los datos que necesitamos de la API
data class WeatherInfo(
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val precipitationProbability: Int
)

// Modelos para parsear la respuesta JSON de la API
data class OpenMeteoResponse(
    val current_weather: CurrentWeather,
    val hourly: Hourly // Añadimos el objeto 'hourly'
)

data class CurrentWeather(
    val temperature: Double,
    val weathercode: Int,
    val windspeed: Double // El viento lo obtenemos de aquí
)

data class Hourly(
    val relativehumidity_2m: List<Int>,
    val precipitation_probability: List<Int>
)

// Instancias reutilizables
private val client = OkHttpClient()
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Función de suspensión para obtener el clima
suspend fun getWeather(latitude: Double, longitude: Double): WeatherInfo? {
    // URL actualizada para pedir humedad y probabilidad de precipitación
    val url = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current_weather=true&hourly=relativehumidity_2m,precipitation_probability&forecast_days=1"
    val request = Request.Builder().url(url).build()

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
            // Tomamos el primer valor de la lista para humedad y precipitación
            val humidity = weatherResponse.hourly.relativehumidity_2m.firstOrNull() ?: 0
            val precipitation = weatherResponse.hourly.precipitation_probability.firstOrNull() ?: 0

            WeatherInfo(
                temperature = weatherResponse.current_weather.temperature,
                description = weatherCodeToDescription(weatherResponse.current_weather.weathercode),
                humidity = humidity,
                windSpeed = weatherResponse.current_weather.windspeed,
                precipitationProbability = precipitation
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

suspend fun getCityName(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        @Suppress("DEPRECATION")
        // CAMBIO: Pedimos hasta 5 resultados para aumentar la probabilidad de encontrar la ciudad.
        val addresses = geocoder.getFromLocation(latitude, longitude, 5)
        if (addresses != null && addresses.isNotEmpty()) {
            // Buscamos en todos los resultados el primer 'subAdminArea' que no sea nulo.
            val city = addresses.firstNotNullOfOrNull { it.subAdminArea }

            // Si encontramos la ciudad, la usamos. Si no, como último recurso,
            // usamos la 'locality' del primer resultado.
            city ?: addresses[0]?.locality ?: "Ubicación desconocida"
        } else {
            "Ubicación desconocida"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Ubicación desconocida"
    }
}


// Función auxiliar para convertir OkHttp a suspend
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