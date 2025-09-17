package com.example.agromo.login_ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.agromo.login_ui.components.PrimaryButton
import com.example.agromo.login_ui.components.TextFieldOutlined

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val customGreen = Color(0xFF317C42)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextFieldOutlined(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            placeholder = "Ingresa tu correo"
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Enviar",
            onClick = { /* TODO: Lógica de recuperar contraseña */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onBackToLogin,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = customGreen
            )
        ) {
            Text("Volver al login")
        }
    }
}
