package com.example.agromo.formulario_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agromo.formulario.data.FormularioEntity

@Composable
fun FormularioDetailScreen(formulario: FormularioEntity) {
    Column(Modifier.padding(16.dp)) {
        Text("Detalle del formulario", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("ID: ${formulario.id}")
        Text("Ubicación: ${formulario.ubicacion}")
        Text("Cultivo: ${formulario.cultivo}")
        Text("Humedad: ${formulario.humedad}")
    }
}
