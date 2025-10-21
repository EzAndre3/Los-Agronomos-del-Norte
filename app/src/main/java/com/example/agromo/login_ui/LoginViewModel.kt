package com.example.agromo.login_ui

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.model.LoginRequest
import com.example.agromo.network.ApiClient
import com.example.agromo.network.SessionManager
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.ui.graphics.Color

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var rememberMe by mutableStateOf(false)

    var loginMessage by mutableStateOf("")
    var loginMessageColor by mutableStateOf(Color.Red)
    var isLoginSuccessful by mutableStateOf(false)

    var username by mutableStateOf("")
        private set

    private val sessionManager = SessionManager(application.applicationContext)

    init {
        // 🔹 Si existe sesión previa, recuperarla
        viewModelScope.launch {
            email = sessionManager.getSavedEmail() ?: ""
            rememberMe = email.isNotEmpty()
        }
    }

    fun login() {
        loginMessage = ""
        isLoginSuccessful = false

        // 🔹 Validación simple
        if (email.isBlank() || password.isBlank()) {
            loginMessage = "Ingresa usuario y contraseña"
            loginMessageColor = Color.Red
            return
        }

        viewModelScope.launch {
            try {
                val request = LoginRequest(
                    user_email = email.trim(),
                    password = password.trim()
                )

                Log.d("LOGIN", "Iniciando login con ${request.user_email}")
                val response = ApiClient.apiService.loginUser(request)

                Log.d("LOGIN", "HTTP code: ${response.code()}")
                Log.d("LOGIN", "isSuccessful: ${response.isSuccessful}")
                Log.d("LOGIN", "Body: ${response.body()}")

                if (response.isSuccessful) {
                    val token = response.body()?.token ?: ""
                    val usernameBackend = response.body()?.user?.username ?: ""
                    username = usernameBackend

                    // ✅ Guardar token y sesión de forma consistente
                    if (rememberMe) {
                        sessionManager.saveToken(token)
                        sessionManager.saveEmail(email)
                    } else {
                        // Si no se selecciona "Recuérdame", aún guardamos token temporal
                        sessionManager.saveToken(token)
                        sessionManager.clearEmail()
                    }

                    // ✅ Siempre guardar username (no afecta la sesión)
                    sessionManager.saveUsername(usernameBackend)

                    Log.d("LOGIN", "Token guardado: $token")
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
