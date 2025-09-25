package com.example.agromo.login_ui

import android.util.Log
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
import com.example.agromo.model.LoginRequest
//import com.example.agromo.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    var loginMessage by remember { mutableStateOf("") }
    var loginMessageColor by remember { mutableStateOf(Color.Red) }

    val customGreen = Color(0xFF317C42)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Campo de correo
        TextFieldOutlined(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            placeholder = "Ingresa tu correo"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
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
            onClick = {
                loginMessage = ""

                scope.launch {
                    try {
                        // TODO: Se comenta la llamada a la API
                        /*
                        // Enviar POST con body JSON
                        val request = LoginRequest(email.trim(), password.trim())
                        val response = ApiClient.apiService.login(request)

                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null && body.success) {
                                loginMessage = body.message ?: "Inicio de sesión exitoso"
                                loginMessageColor = customGreen
                                Log.d("LoginDebug", "Login exitoso, token: ${body.token}")
                                // Aquí guardarías el token de forma segura
                            } else {
                                loginMessage = body?.message ?: "Credenciales incorrectas"
                                loginMessageColor = Color.Red
                                Log.d("LoginDebug", "Login fallido para correo: $email")
                            }
                        } else {
                            loginMessage = "Error: ${response.code()}"
                            loginMessageColor = Color.Red
                            Log.e("LoginDebug", "Error HTTP: ${response.code()}")
                        }
                        */

                    } catch (e: Exception) {
                        // loginMessage = "Error de conexión"
                        // loginMessageColor = Color.Red
                        // Log.e("LoginDebug", "Error: ${e.message}")
                    }
                }
            }
        )

        /*
        // Mostrar mensaje de login
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
                colors = ButtonDefaults.textButtonColors(contentColor = customGreen)
            ) {
                Text("Regístrate")
            }
        }
    }
}
