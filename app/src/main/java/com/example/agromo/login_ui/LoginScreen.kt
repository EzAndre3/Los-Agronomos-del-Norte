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
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

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

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "Ingresa tu contraseña"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = customGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recuérdame",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            TextButton(
                onClick = { /* TODO */ },
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = customGreen
                )
            ) {
                Text("Olvidé mi contraseña")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Iniciar Sesión",
            onClick = { /* TODO */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿No tienes cuenta?")
            Spacer(modifier = Modifier.width(6.dp))
            TextButton(
                onClick = { /* TODO */ },
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = customGreen
                )
            ) {
                Text("Regístrate")
            }
        }
    }
}
