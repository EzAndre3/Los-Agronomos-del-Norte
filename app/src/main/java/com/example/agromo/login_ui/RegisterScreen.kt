package com.example.agromo.login_ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.agromo.login_ui.components.PrimaryButton
import com.example.agromo.login_ui.components.PasswordField
import com.example.agromo.login_ui.components.TextFieldOutlined

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val customGreen = Color(0xFF317C42)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextFieldOutlined(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Nombre completo",
            placeholder = "Ingresa tu nombre"
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldOutlined(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            placeholder = "Ingresa tu correo"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "Ingresa tu contraseña"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar Contraseña",
            placeholder = "Repite tu contraseña"
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Registrarse",
            onClick = {
                // TODO: Lógica de registro
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿Ya tienes cuenta?")
            Spacer(modifier = Modifier.width(6.dp))
            TextButton(
                onClick = onBackToLogin,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = customGreen
                )
            ) {
                Text("Inicia Sesión")
            }
        }
    }
}
