package com.example.agromo.formulario

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.agromo.data.FormularioEntity

class RegistroFormularioViewModel(private val repository: FormularioRepository) : ViewModel() {

    private val _formulario = mutableStateOf(FormularioEntity())
    val formulario: State<FormularioEntity> = _formulario

    fun updateLocalizacion(value: String) {
        _formulario.value = _formulario.value.copy(localizacion = value)
    }

    fun updateCultivo(value: String) {
        _formulario.value = _formulario.value.copy(cultivo = value)
    }

    fun updateFechaSiembra(value: String) {
        _formulario.value = _formulario.value.copy(fecha_siembra = value)
    }

    fun updateHumedad(value: String) {
        _formulario.value = _formulario.value.copy(humedad = value)
    }

    fun updatePH(value: String, metodo: String) {
        _formulario.value = _formulario.value.copy(ph = value, metodo_ph = metodo)
    }

    fun updateAltura(value: String, metodo: String) {
        _formulario.value = _formulario.value.copy(altura_planta = value, metodo_altura = metodo)
    }

    fun updateFenologico(estado: String, observaciones: String) {
        _formulario.value = _formulario.value.copy(
            estado_fenologico = estado,
            observaciones = observaciones
        )
    }

    fun updateFollaje(densidad: String, color: String, estado: String) {
        _formulario.value = _formulario.value.copy(
            densidad_follaje = densidad,
            color_follaje = color,
            estado_follaje = estado
        )
    }

    fun guardarFormulario(): FormularioEntity {
        return _formulario.value
    }

    fun updateImagen(uri: String) {
        _formulario.value = _formulario.value.copy(imagenUri = uri)
    }
}
