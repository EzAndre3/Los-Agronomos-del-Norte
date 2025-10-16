package com.example.agromo.formulario

import androidx.lifecycle.ViewModel
import com.example.agromo.formulario.FormularioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FormularioListViewModel : ViewModel() {

    // Estado principal del formulario
    private val _formulario = MutableStateFlow(FormularioEntity())
    val formulario: StateFlow<FormularioEntity> = _formulario

    // 🔹 Actualizaciones de campos
    fun updateUbicacion(ubicacion: String) {
        _formulario.value = _formulario.value.copy(ubicacion = ubicacion)
    }

    fun updateCultivo(cultivo: String) {
        _formulario.value = _formulario.value.copy(cultivo = cultivo)
    }

    fun updateFechaSiembra(fecha: String) {
        _formulario.value = _formulario.value.copy(fechaSiembra = fecha)
    }

    fun updateHumedad(humedad: String) {
        _formulario.value = _formulario.value.copy(humedadTierra = humedad)
    }

    fun updatePH(ph: String, metodo: String) {
        _formulario.value = _formulario.value.copy(ph = ph, metodoPH = metodo)
    }

    fun updateAltura(altura: String, metodo: String) {
        _formulario.value = _formulario.value.copy(
            alturaPlanta = altura,
            metodoAltura = metodo
        )
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
        _formulario.value = _formulario.value.copy(
            nitrogeno = if (tipo == "N") valor else _formulario.value.nitrogeno,
            fosforo = if (tipo == "P") valor else _formulario.value.fosforo,
            potasio = if (tipo == "K") valor else _formulario.value.potasio,
            materiaOrganica = if (tipo == "MO") valor else _formulario.value.materiaOrganica,
            cic = if (tipo == "CIC") valor else _formulario.value.cic
        )
    }

    // 🔹 Guardar/obtener formulario
    fun guardarFormulario(): FormularioEntity = _formulario.value
}
