package com.example.agromo.login_ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.model.LoginRequest
import com.example.agromo.network.ApiClient
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.ui.graphics.Color

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var rememberMe by mutableStateOf(false)

    var loginMessage by mutableStateOf("")
    var loginMessageColor by mutableStateOf(Color.Red)
    var isLoginSuccessful by mutableStateOf(false)

    fun login() {
        loginMessage = ""
        isLoginSuccessful = false

        // 🔹 Validación básica
        if (email.isBlank() || password.isBlank()) {
            loginMessage = "Ingresa usuario y contraseña"
            loginMessageColor = Color.Red
            return
        }

        viewModelScope.launch {
            try {
                // 🔹 Crear request con los datos correctos
                val request = LoginRequest(
                    user_email = email.trim(),
                    password = password.trim()
                )

                Log.d("LOGIN", "Iniciando login con ${request.user_email}")

                // 🔹 Llamada al API
                val response = ApiClient.apiService.loginUser(request)

                // 🔹 Logs para depurar
                Log.d("LOGIN", "HTTP code: ${response.code()}")
                Log.d("LOGIN", "isSuccessful: ${response.isSuccessful}")
                Log.d("LOGIN", "Body: ${response.body()}")
                Log.d("LOGIN", "ErrorBody: ${response.errorBody()?.string()}")

                // 🔹 Manejo de respuesta
                if (response.isSuccessful) {
                    loginMessage = "Inicio de sesión exitoso"
                    loginMessageColor = Color(0xFF317C42)
                    isLoginSuccessful = true
                } else {
                    loginMessage = "Credenciales incorrectas (${response.code()})"
                    loginMessageColor = Color.Red
                }

            } catch (e: Exception) {
                Log.e("LOGIN", "Error de conexión", e)
                loginMessage = "Error de conexión: ${e.localizedMessage}"
                loginMessageColor = Color.Red
            }
        }
    }
}
