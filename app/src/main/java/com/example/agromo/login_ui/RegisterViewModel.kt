package com.example.agromo.login_ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agromo.model.RegisterRequest
import com.example.agromo.network.ApiClient
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.ui.graphics.Color

class RegisterViewModel : ViewModel() {

    var fullName by mutableStateOf("")
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var registerMessage by mutableStateOf("")
    var registerMessageColor by mutableStateOf(Color.Red)
    var isRegisterSuccessful by mutableStateOf(false)

    fun register() {
        registerMessage = ""
        isRegisterSuccessful = false

        // 🔹 Validaciones básicas
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            registerMessage = "Completa todos los campos"
            registerMessageColor = Color.Red
            return
        }

        if (password != confirmPassword) {
            registerMessage = "Las contraseñas no coinciden"
            registerMessageColor = Color.Red
            return
        }

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    username = username.trim(),
                    password = password.trim(),
                    user_email = email.trim()
                )

                Log.d("REGISTER", "Registrando usuario: ${request.username}")

                val response = ApiClient.apiService.registerUser(request)

                Log.d("REGISTER", "HTTP code: ${response.code()}")
                Log.d("REGISTER", "isSuccessful: ${response.isSuccessful}")
                Log.d("REGISTER", "Body: ${response.body()}")
                Log.d("REGISTER", "ErrorBody: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    registerMessage = response.body()?.message ?: "Registro exitoso"
                    registerMessageColor = Color(0xFF317C42)
                    isRegisterSuccessful = true
                } else {
                    registerMessage = "Error al registrar (${response.code()})"
                    registerMessageColor = Color.Red
                }

            } catch (e: Exception) {
                Log.e("REGISTER", "Error de conexión", e)
                registerMessage = "Error de conexión: ${e.localizedMessage}"
                registerMessageColor = Color.Red
            }
        }
    }
}
