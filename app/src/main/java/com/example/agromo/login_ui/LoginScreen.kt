package com.example.agromo.login_ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agromo.general_components.LogoAgromo
import com.example.agromo.login_ui.components.PrimaryButton
import com.example.agromo.login_ui.components.PasswordField

@Composable
fun LoginScreen(
    onNavigateToDashboard: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel() // 👈 Inyectamos el ViewModel
) {
    val defaultGreen = Color(0xFF344E18)

    // Obtenemos los estados del ViewModel
    val email by remember { viewModel::email }
    val password by remember { viewModel::password }
    val rememberMe by remember { viewModel::rememberMe }
    val loginMessage by remember { viewModel::loginMessage }
    val loginMessageColor by remember { viewModel::loginMessageColor }
    val isLoginSuccessful by remember { viewModel::isLoginSuccessful }

    // Si el login fue exitoso, navegamos
    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful) {
            onNavigateToDashboard()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        LogoAgromo(size = 120.dp)

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de correo
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.email = it },
            label = { Text("Correo Electrónico") },
            placeholder = { Text("Ingresa tu correo") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        PasswordField(
            value = password,
            onValueChange = { viewModel.password = it },
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
                    onCheckedChange = { viewModel.rememberMe = it },
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
            onClick = { viewModel.login() }
        )

        // Mostrar mensaje del login
        if (loginMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = loginMessage, color = loginMessageColor)
        }

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
