package com.example.agromo.formulario

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ✅ Utilidades para fecha y hora actuales
val fechaActual: String
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

val horaActual: String
    get() = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

// ✅ Entidad principal usada por Room
@Entity(tableName = "formulario")
data class FormularioEntity(
    @PrimaryKey val id: String = "FORM-" + System.currentTimeMillis(),

    // 🔹 Datos generales
    val ubicacion: String = "",
    val cultivo: String = "",
    val fechaSiembra: String = "",

    // 🔹 Datos de suelo
    val humedadTierra: String = "",
    val ph: String = "",
    val metodoPH: String = "",

    // 🔹 Datos de planta
    val alturaPlanta: String = "",
    val metodoAltura: String = "",
    val estadoFenologico: String = "",
    val nivelFertilidadManual: String = "",
    val nitrogeno: String = "",
    val fosforo: String = "",
    val potasio: String = "",
    val materiaOrganica: String = "",
    val cic: String = "",

    // 🔹 Follaje
    val densidadFollaje: String = "",
    val colorFollaje: String = "",
    val estadoFollaje: String = "",

    // 🔹 Extras
    val observaciones: String = "",
    val fechaRegistro: String = fechaActual,
    val horaRegistro: String = horaActual,
    val estadoConexion: Boolean = true


)
