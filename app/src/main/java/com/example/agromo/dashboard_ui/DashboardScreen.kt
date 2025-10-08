package com.example.agromo.dashboard_ui
import com.example.agromo.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Eliminamos la importación de NavController ya que no lo usaremos directamente aquí
// import androidx.navigation.NavController

@Composable
fun DashboardScreen(
    onNavigateBackToLogin: () -> Unit,
    onNavigateToAiChat: () -> Unit,
    onNavigateToFormulario: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_plant),
                contentDescription = "Logo",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Agromo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta con progreso
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cloud_upload),
                    contentDescription = "Progreso",
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = 0.68f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Color(0xFF2E7D32),
                    trackColor = Color(0xFFBDBDBD)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("68%", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Text("488 registros")
                    Text("156 en nube")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botones principales con navegación
        Button(
            onClick = { onNavigateToFormulario() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Nuevo Formulario", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onNavigateToAiChat() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("IA - Cultivos", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { /* acción de sincronización */ },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2E7D32))
        ) {
            Text("Sincronizar", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de configuración con navegación
        IconButton(
            onClick = { onNavigateBackToLogin() }, // Modificado: Llamamos a la función
            modifier = Modifier
                .size(60.dp)
                .background(Color(0xFF2E7D32), shape = RoundedCornerShape(16.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Configuración",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
