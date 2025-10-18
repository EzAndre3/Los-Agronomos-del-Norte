package com.example.agromo.dashboard_ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.data.FormularioDao
import com.example.agromo.data.FormularioEntity
import com.example.agromo.location.getLastKnownLocation
import com.example.agromo.weather.getCityName
import com.example.agromo.weather.getWeather
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.location.Location

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

                    _weatherState.value = WeatherState(
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

data class WeatherState(
    val locationName: String = "Cargando...", // Hardcoded for now
    val temperature: String = "--",
    val description: String = "Cargando...",
    val humidity: String = "--",
    val windSpeed: String = "--",
    val precipitation: String = "--",
    val sprayStatus: SprayStatus = SprayStatus.UNKNOWN // Logic not implemented yet
)

enum class SprayStatus {
    FAVORABLE, UNFAVORABLE, UNKNOWN
}
