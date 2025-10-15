package com.example.agromo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val fechaActual: String
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

val horaActual: String
    get() = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

@Entity(tableName = "formulario")
data class FormularioEntity(
    @PrimaryKey val id: String = "FORM-" + System.currentTimeMillis(),
    val ubicacion: String = "",
    val cultivo: String = "",
    val fechaSiembra: String? = null,
    val humedadTierra: String? = null,
    val ph: String? = null,
    val metodoPH: String? = null,
    val alturaPlanta: String? = null,
    val metodoAltura: String? = null,
    val estadoFenologico: String? = null,
    val observaciones: String? = null,
    val densidadFollaje: String? = null,
    val colorFollaje: String? = null,
    val estadoFollaje: String? = null,
    val fechaRegistro: String = fechaActual,
    val horaRegistro: String = horaActual,
    val estadoConexion: Boolean = true
)

