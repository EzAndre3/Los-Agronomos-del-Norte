package com.example.agromo.dashboard_ui

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.location.getLastKnownLocation
import com.example.agromo.weather.getCityName
import com.example.agromo.weather.getWeather
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estado actualizado con todos los datos para la UI
data class DashboardWeatherState(
    val locationName: String = "Cargando...", // Texto inicial
    val temperature: String = "--ºC",
    val description: String = "--",
    val humidity: String = "--%",
    val windSpeed: String = "-- Km/h",
    val precipitation: String = "--%",
    val sprayStatus: SprayStatus = SprayStatus.UNKNOWN
)

enum class SprayStatus {
    FAVORABLE, UNFAVORABLE, UNKNOWN
}

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
    private val _weatherState = MutableStateFlow(DashboardWeatherState())
    val weatherState: StateFlow<DashboardWeatherState> get() = _weatherState

    fun loadWeather() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val location: Location? = getLastKnownLocation(context)
            if (location != null) {

                // <<< LÓGICA MODIFICADA AQUÍ >>>
                // Ejecuta la obtención del clima y el nombre de la ciudad en paralelo
                val weatherJob = async { getWeather(location.latitude, location.longitude) }
                val cityJob = async { getCityName(context, location.latitude, location.longitude) }

                // Espera a que ambos terminen
                val weather = weatherJob.await()
                val cityName = cityJob.await()

                if (weather != null) {
                    val isFavorable = weather.windSpeed < 15 && weather.precipitationProbability < 20
                    val status = if (isFavorable) SprayStatus.FAVORABLE else SprayStatus.UNFAVORABLE

                    _weatherState.value = DashboardWeatherState(
                        locationName = cityName, // Usamos el nombre de la ciudad obtenido
                        temperature = "${weather.temperature.toInt()}ºC",
                        description = weather.description,
                        humidity = "${weather.humidity}%",
                        windSpeed = "${weather.windSpeed.toInt()} Km/h",
                        precipitation = "${weather.precipitationProbability}%",
                        sprayStatus = status
                    )
                } else {
                    // Si el clima falla, al menos mostramos la ciudad
                    _weatherState.value = _weatherState.value.copy(locationName = cityName)
                }
            }
        }
    }
}