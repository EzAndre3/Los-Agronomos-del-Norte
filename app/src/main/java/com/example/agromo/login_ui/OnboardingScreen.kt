package com.example.agromo.login_ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.agromo.R

@Composable
fun OnboardingScreen(onFinish: () -> Unit = {}) {

    val greenColor = Color(0xFF344E18)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Bienvenido a Agromo!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = greenColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            color = greenColor.copy(alpha = 0.1f),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Mantenga todos los parámetros de seguimiento de sus cultivos al día y consulte el saber local con la comunidad",
                color = greenColor,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.welcome_agromo),
            contentDescription = "Imagen de agricultor",
            modifier = Modifier
                .size(220.dp)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onFinish() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF344E18))
        ) {
            Text("Empezar", color = Color.White)
        }
    }
}
