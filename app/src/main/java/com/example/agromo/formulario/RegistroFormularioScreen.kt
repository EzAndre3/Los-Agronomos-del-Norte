package com.example.agromo.formulario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistroFormularioScreen(
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var fecha by remember { mutableStateOf(TextFieldValue("")) }
    var hora by remember { mutableStateOf(TextFieldValue("")) }
    var operador by remember { mutableStateOf(TextFieldValue("")) }

    var temp by remember { mutableStateOf(25f) }
    var humedad by remember { mutableStateOf(60f) }
    var viento by remember { mutableStateOf(10f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .padding(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Nuevo Formulario", fontSize = 22.sp, style = MaterialTheme.typography.titleLarge)
            Row {
                OutlinedButton(onClick = { /* limpiar */ }) {
                    Text("Borrar")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { onBack() }) {
                    Text("Guardar")
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Scroll con las secciones
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Información básica
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Información Básica", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)

                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del Formulario *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { fecha = it },
                        label = { Text("Fecha *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = hora,
                        onValueChange = { hora = it },
                        label = { Text("Hora *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = operador,
                        onValueChange = { operador = it },
                        label = { Text("Nombre del Operador") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Condiciones Climáticas
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Condiciones Climáticas", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)

                    Spacer(Modifier.height(12.dp))

                    Text("Temperatura (°C): ${temp.toInt()}")
                    Slider(
                        value = temp,
                        onValueChange = { temp = it },
                        valueRange = -10f..50f
                    )

                    Text("Humedad (%): ${humedad.toInt()}")
                    Slider(
                        value = humedad,
                        onValueChange = { humedad = it },
                        valueRange = 0f..100f
                    )

                    Text("Velocidad del Viento (km/h): ${viento.toInt()}")
                    Slider(
                        value = viento,
                        onValueChange = { viento = it },
                        valueRange = 0f..100f
                    )
                }
            }

            // Información del Cultivo
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Información del Cultivo", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    // Aquí puedes agregar botones de selección de cultivo con íconos
                    Text("⚠️ Aquí van botones de tipo de cultivo")
                }
            }

            // Detalles Químicos
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Detalles Químicos", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    // Aquí puedes agregar chips o botones de selección
                    Text("⚠️ Aquí van opciones de químicos y método de aplicación")
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}