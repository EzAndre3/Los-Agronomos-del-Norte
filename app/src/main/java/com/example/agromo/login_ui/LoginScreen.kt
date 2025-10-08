package com.example.agromo.login_ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.agromo.general_components.LogoAgromo
import com.example.agromo.login_ui.components.PrimaryButton
import com.example.agromo.login_ui.components.PasswordField

@Composable
fun LoginScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Estados locales temporales hasta integrar ViewModel
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    // var loginMessage by remember { mutableStateOf("") } // Para mostrar mensajes de login

    val defaultGreen = Color(0xFF344E18) // Mismo verde que PrimaryButton

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Logo Agromo
        LogoAgromo(size = 120.dp)

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de correo
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            placeholder = { Text("Ingresa tu correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña usando tu PasswordField
        PasswordField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "Ingresa tu contraseña"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox y "Olvidé mi contraseña"
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = defaultGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recuérdame")
            }

            TextButton(
                onClick = onNavigateToForgotPassword,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = defaultGreen)
            ) {
                Text("Olvidé mi contraseña")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de login
        PrimaryButton(
            text = "Iniciar Sesión",
            onClick = {
                // Cuando se integre ViewModel, reemplazar con:
                // viewModel.login()
                onNavigateToDashboard()
            }
        )

        // Mostrar mensaje de login cuando se use ViewModel
        /*
        if (loginMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = loginMessage, color = loginMessageColor)
        }
        */

        Spacer(modifier = Modifier.height(16.dp))

        // Registro
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿No tienes cuenta?")
            Spacer(modifier = Modifier.width(6.dp))
            TextButton(
                onClick = onNavigateToRegister,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = defaultGreen)
            ) {
                Text("Regístrate")
            }
        }
    }
}
