package com.example.agromo.login_ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agromo.login_ui.components.PrimaryButton
import com.example.agromo.login_ui.components.PasswordField
import com.example.agromo.login_ui.components.TextFieldOutlined

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
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

        // 👇 Campo nuevo para el username
        TextFieldOutlined(
            value = username,
            onValueChange = { username = it },
            label = "Username",
            placeholder = "Ingresa tu nombre de usuario"
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
                // Llama al ViewModel
                viewModel.username = username
                viewModel.email = email
                viewModel.password = password
                viewModel.confirmPassword = confirmPassword
                viewModel.register()
            }
        )

        Text(
            text = viewModel.registerMessage,
            color = viewModel.registerMessageColor,
            modifier = Modifier.padding(top = 12.dp)
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
