package com.example.agromo.formulario

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.agromo.data.FormularioEntity

class RegistroFormularioViewModel(private val repository: FormularioRepository) : ViewModel() {

    private val _formulario = mutableStateOf(FormularioEntity())
    val formulario: State<FormularioEntity> = _formulario

    fun updateUbicacion(value: String) {
        _formulario.value = _formulario.value.copy(ubicacion = value)
    }

    fun updateCultivo(value: String) {
        _formulario.value = _formulario.value.copy(cultivo = value)
    }

    fun updateFechaSiembra(value: String) {
        _formulario.value = _formulario.value.copy(fechaSiembra = value)
    }

    fun updateHumedad(value: String) {
        _formulario.value = _formulario.value.copy(humedadTierra = value)
    }

    fun updatePH(value: String, metodo: String) {
        _formulario.value = _formulario.value.copy(ph = value, metodoPH = metodo)
    }

    fun updateAltura(value: String, metodo: String) {
        _formulario.value = _formulario.value.copy(alturaPlanta = value, metodoAltura = metodo)
    }

    fun updateFenologico(estado: String, observaciones: String) {
        _formulario.value = _formulario.value.copy(
            estadoFenologico = estado,
            observaciones = observaciones
        )
    }

    fun updateFollaje(densidad: String, color: String, estado: String) {
        _formulario.value = _formulario.value.copy(
            densidadFollaje = densidad,
            colorFollaje = color,
            estadoFollaje = estado
        )
    }

    fun guardarFormulario(): FormularioEntity {
        return _formulario.value
    }

    fun updateImagen(uri: String) {
        _formulario.value = _formulario.value.copy(imagenUri = uri)
    }
}
