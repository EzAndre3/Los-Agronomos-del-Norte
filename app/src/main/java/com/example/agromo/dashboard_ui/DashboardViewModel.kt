package com.example.agromo.dashboard_ui

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.location.getLastKnownLocation
import com.example.agromo.weather.getWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext // Importación necesaria

data class DashboardWeatherState(
    val locationName: String = "Ubicación desconocida",
    val temperature: String = "--ºC",
    val description: String = "--"
)

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
    private val _weatherState = MutableStateFlow(DashboardWeatherState())
    val weatherState: StateFlow<DashboardWeatherState> get() = _weatherState

    fun loadWeather() {
        // Ya no necesitamos Dispatchers.IO porque getWeather es una suspend function "main-safe"
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            val location: Location? = getLastKnownLocation(context)
            if (location != null) {
                // La llamada a getWeather ahora es una suspend call
                val weather = getWeather(location.latitude, location.longitude)
                if (weather != null) {
                    // La actualización del estado ya está en el hilo principal por defecto en viewModelScope.launch
                    _weatherState.value = DashboardWeatherState(
                        locationName = "Tu ubicación",
                        temperature = "${weather.temperature}ºC",

                        )
                }
            }
        }
    }
}


