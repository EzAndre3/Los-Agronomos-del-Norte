package com.example.agromo.ui.form

import androidx.lifecycle.ViewModel
import com.example.agromo.data.FormularioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegistroFormularioViewModel : ViewModel() {

    private val _formulario = MutableStateFlow(FormularioEntity())
    val formulario: StateFlow<FormularioEntity> = _formulario

    fun updateUbicacion(ubicacion: String) {
        _formulario.value = _formulario.value.copy(ubicacion = ubicacion)
    }

    fun updateCultivo(cultivo: String, fechaSiembra: String?) {
        _formulario.value = _formulario.value.copy(cultivo = cultivo, fechaSiembra = fechaSiembra)
    }

    fun updateHumedad(humedad: String) {
        _formulario.value = _formulario.value.copy(humedadTierra = humedad)
    }

    fun updatePH(ph: String, metodo: String?) {
        _formulario.value = _formulario.value.copy(ph = ph, metodoPH = metodo)
    }

    fun updateAltura(altura: String, metodo: String?) {
        _formulario.value = _formulario.value.copy(alturaPlanta = altura, metodoAltura = metodo)
    }

    fun updateFenologico(estado: String, observaciones: String?) {
        _formulario.value = _formulario.value.copy(
            estadoFenologico = estado,
            observaciones = observaciones
        )
    }

    fun updateFollaje(densidad: String?, color: String?, estado: String?) {
        _formulario.value = _formulario.value.copy(
            densidadFollaje = densidad,
            colorFollaje = color,
            estadoFollaje = estado
        )
    }

    fun guardarFormulario(): FormularioEntity {
        return _formulario.value
    }
}
