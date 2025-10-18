package com.example.agromo.dashboard_ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.data.FormularioDao
import com.example.agromo.data.FormularioEntity
import com.example.agromo.location.getLastKnownLocation
import com.example.agromo.weather.getCityName
import com.example.agromo.weather.getWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.location.Location
import kotlinx.coroutines.withContext

class DashboardViewModel(application: Application, private val formularioDao: FormularioDao) : AndroidViewModel(application) {

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    val formularios: StateFlow<List<FormularioEntity>> = formularioDao.getAllFormularios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadWeather() {
        // Lanza toda la operación en un hilo de fondo para no bloquear jamás el hilo principal.
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext
            try {
                val location: Location? = getLastKnownLocation(context)

                if (location != null) {
                    val weatherJob = async { getWeather(location.latitude, location.longitude) }
                    val cityJob = async { getCityName(context, location.latitude, location.longitude) }

                    val weather = weatherJob.await()
                    val cityName = cityJob.await()

                    val newState = if (weather != null) {
                        val isFavorable = weather.windSpeed < 15 && weather.precipitationProbability < 20
                        val status = if (isFavorable) SprayStatus.FAVORABLE else SprayStatus.UNFAVORABLE

                        WeatherState(
                            locationName = cityName,
                            temperature = "${weather.temperature.toInt()}ºC",
                            description = weather.description,
                            humidity = "${weather.humidity}%",
                            windSpeed = "${weather.windSpeed.toInt()} Km/h",
                            precipitation = "${weather.precipitationProbability}%",
                            sprayStatus = status
                        )
                    } else {
                        WeatherState(
                            locationName = cityName,
                            temperature = "--",
                            description = "No se pudo obtener el clima",
                            humidity = "--",
                            windSpeed = "--",
                            precipitation = "--",
                            sprayStatus = SprayStatus.UNKNOWN
                        )
                    }
                    // Una vez que todoel trabajo está hecho, actualiza la UI en el hilo principal.
                    withContext(Dispatchers.Main) {
                        _weatherState.value = newState
                    }
                } else {
                    // Si no hay ubicación, crea el estado de error y actualiza la UI.
                    val noLocationState = WeatherState(
                        locationName = "Ubicación desconocida",
                        temperature = "--",
                        description = "Active la ubicación para obtener el clima",
                        humidity = "--",
                        windSpeed = "--",
                        precipitation = "--",
                        sprayStatus = SprayStatus.UNKNOWN
                    )
                    withContext(Dispatchers.Main) {
                        _weatherState.value = noLocationState
                    }
                }
            } catch (e: Exception) {
                // Si cualquier cosa falla (red, etc.), crea el estado de error y actualiza la UI.
                val errorState = WeatherState(
                    locationName = "Sin conexión",
                    temperature = "--",
                    description = "No se pudo cargar el clima",
                    humidity = "--",
                    windSpeed = "--",
                    precipitation = "--",
                    sprayStatus = SprayStatus.UNKNOWN
                )
                withContext(Dispatchers.Main) {
                    _weatherState.value = errorState
                }
            }
        }
    }
}

data class WeatherState(
    val locationName: String = "Cargando...",
    val temperature: String = "--",
    val description: String = "Cargando...",
    val humidity: String = "--",
    val windSpeed: String = "--",
    val precipitation: String = "--",
    val sprayStatus: SprayStatus = SprayStatus.UNKNOWN
)

enum class SprayStatus {
    FAVORABLE, UNFAVORABLE, UNKNOWN
}
