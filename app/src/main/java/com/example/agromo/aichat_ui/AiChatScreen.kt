package com.example.agromo.aichat_ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.agromo.R

@Composable
fun AiChatScreen(
    onNavigateBackToDashboard: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Columna 1
        Column(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo + Título
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plant),
                    contentDescription = "Logo",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(40.dp)
                )

                Text(
                    text = "Agromo",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF2E7D32)
                )
            }
            // Subtitulo / Slogan
            Text(
                text = "Innovación Inteligente para la agricultura",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Columna 2
        Column(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título analisis
            Text(
                text = "Análisis de Cultivos",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Preview de imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp)
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Preview",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones Seleccionar archivo y Abrir camara
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Seleccionar Archivo")
                }
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Abrir Cámara")
                }
            }
        }

        // Columna 3
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Respuesta de la IA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(5.dp))
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .padding(top = 5.dp, bottom = 2.5.dp)
                ) {
                    Text(
                        text = "Respuesta De La IA",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                Box(
                    modifier = Modifier
                        .drawBehind {
                            // línea arriba
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 6f
                            )
                        }
                        .padding(horizontal = 5.dp)
                        .padding(top = 2.5.dp, bottom = 5.dp)
                ) {
                    Text(
                        text = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas."
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones Regresar y Nuevo analisis
            Row(
                horizontalArrangement = Arrangement.spacedBy(70.dp)
            ) {
                Button(
                    onClick = { onNavigateBackToDashboard() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Regresar")
                }
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Nuevo Analisis")
                }
            }
        }
        // Columna 4 (para alinear)
        Column(
            modifier = Modifier
                .weight(.2f)
                .fillMaxSize(),
        ) {  }
    }
}
