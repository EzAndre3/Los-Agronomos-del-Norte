package com.example.agromo.login_ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.model.LoginRequest
// import com.example.agromo.network.ApiClient // Descomenta cuando tengas la API lista
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

class LoginViewModel : ViewModel() {

    // Estados observables desde el Composable
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var rememberMe by mutableStateOf(false)

    var loginMessage by mutableStateOf("")
    var loginMessageColor by mutableStateOf(Color.Red)
    var isLoginSuccessful by mutableStateOf(false)

    private val customGreen = Color(0xFF317C42)

    fun login() {
        loginMessage = ""
        isLoginSuccessful = false

        // Aquí puedes validar localmente antes de llamar a la API
        if (email.isBlank() || password.isBlank()) {
            loginMessage = "Por favor ingresa tus credenciales"
            loginMessageColor = Color.Red
            return
        }

        // Simulación o llamada real a API
        viewModelScope.launch {
            try {
                //  Aquí pondrás la llamada real cuando tengas ApiClient
                /*
                val request = LoginRequest(email.trim(), password.trim())
                val response = ApiClient.apiService.login(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    loginMessage = "Inicio de sesión exitoso"
                    loginMessageColor = customGreen
                    isLoginSuccessful = true
                } else {
                    loginMessage = "Credenciales incorrectas"
                    loginMessageColor = Color.Red
                }
                */

                // 🔹 Simulación temporal
                if (email == "hola" && password == "1234") {
                    loginMessage = "Inicio de sesión exitoso"
                    loginMessageColor = customGreen
                    isLoginSuccessful = true
                } else {
                    loginMessage = "Credenciales incorrectas"
                    loginMessageColor = Color.Red
                }

            } catch (e: Exception) {
                loginMessage = "Error de conexión"
                loginMessageColor = Color.Red
            }
        }
    }
}
