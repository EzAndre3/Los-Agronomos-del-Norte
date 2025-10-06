package com.example.agromo.login_ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agromo.login_ui.components.PrimaryButton
import com.example.agromo.login_ui.components.PasswordField
import com.example.agromo.login_ui.components.TextFieldOutlined

//import com.example.agromo.network.ApiClient


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {

    val email = viewModel.email
    val password = viewModel.password
    val rememberMe = viewModel.rememberMe
    val loginMessage = viewModel.loginMessage
    val loginMessageColor = viewModel.loginMessageColor
    val isLoginSuccessful = viewModel.isLoginSuccessful

    val customGreen = Color(0xFF317C42)


    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful) onNavigateToDashboard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la app con degradado
        Text(
            text = "agromo",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = LocalTextStyle.current.copy(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF344E18), Color(0xFFA5BE00))
                )
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp)) // Espacio entre título y formulario
        // Campo de correo
        TextFieldOutlined(
            value = email,
            onValueChange = { viewModel.email = it },
            label = "Correo Electrónico",
            placeholder = "Ingresa tu correo"
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
                    colors = CheckboxDefaults.colors(checkedColor = customGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recuérdame")
            }

            TextButton(
                onClick = onNavigateToForgotPassword,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = customGreen)
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

        // Mostrar mensaje
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
                colors = ButtonDefaults.textButtonColors(contentColor = customGreen)
            ) {
                Text("Regístrate")
            }
        }
    }
}
