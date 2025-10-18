package com.example.agromo.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

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

    fun updateFertilidadManual(nivel: String) {
        _formulario.value = _formulario.value.copy(nivelFertilidadManual = nivel)
    }

    fun updateNutriente(tipo: String, valor: String) {
        _formulario.value = when (tipo) {
            "N" -> _formulario.value.copy(nitrogeno = valor)
            "P" -> _formulario.value.copy(fosforo = valor)
            "K" -> _formulario.value.copy(potasio = valor)
            "MO" -> _formulario.value.copy(materiaOrganica = valor)
            "CIC" -> _formulario.value.copy(cic = valor)
            else -> _formulario.value
        }
    }

    fun guardarFormulario(): FormularioEntity {
        return _formulario.value
    }
}
