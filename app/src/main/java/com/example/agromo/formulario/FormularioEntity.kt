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
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,


    val cultivo: String = "",
    val fecha_siembra: String = "",

    val humedad: String = "",
    val metodo_humedad: String = "",

    val ph: String = "",
    val metodo_ph: String = "",

    val altura_planta: String = "",
    val metodo_altura: String = "",
    val estado_fenologico: String = "",

    val densidad_follaje: String = "",
    val color_follaje: String = "",
    val estado_follaje: String = "",


    val observaciones: String = "",

    val fecha_registro: String = fechaActual,
    val hora_registro: String = horaActual,

    val estado: Boolean = true,

    val localizacion: String = "",
    val imagenUri: String = ""
)
