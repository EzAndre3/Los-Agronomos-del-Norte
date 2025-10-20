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

    val cultivo: String = "",
    val fechaSiembra: String = "",

    val humedadTierra: String = "",
    val ph: String = "",
    val metodoPH: String = "",

    val alturaPlanta: String = "",
    val metodoAltura: String = "",
    val estadoFenologico: String = "",
    val nivelFertilidadManual: String = "",
    val nitrogeno: String = "",
    val fosforo: String = "",
    val potasio: String = "",
    val materiaOrganica: String = "",
    val cic: String = "",

    val densidadFollaje: String = "",
    val colorFollaje: String = "",
    val estadoFollaje: String = "",

    val imagenUri: String = "",

    val observaciones: String = "",
    val fechaRegistro: String = fechaActual,
    val horaRegistro: String = horaActual,
    val estadoConexion: Boolean = true
)
