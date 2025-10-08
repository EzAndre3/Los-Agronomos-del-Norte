package com.example.agromo.login_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agromo.general_components.LogoAgromo

@Composable
fun WelcomeScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val greenColor = Color(0xFF6A923E)
    val darkGreen = Color(0xFF344E18)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 100.dp)
        ) {
            LogoAgromo(size = 120.dp)

            // Mensaje descriptivo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(greenColor, RoundedCornerShape(12.dp))
                    .padding(40.dp)
            ) {
                Text(
                    text = "Monitoree sus cultivos y genere informes integrales",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Parte inferior: botones
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
        ) {
            // Botón principal: "Quiero registrarme"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(darkGreen, RoundedCornerShape(12.dp))
                    .clickable { onRegisterClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Quiero registrarme",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            // Botón secundario: "Ya tengo cuenta"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, greenColor, RoundedCornerShape(12.dp))
                    .clickable { onLoginClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ya tengo cuenta",
                    color = greenColor,
                    fontSize = 16.sp
                )
            }
        }
    }
}
