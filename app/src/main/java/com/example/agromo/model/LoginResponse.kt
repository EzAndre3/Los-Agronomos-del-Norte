package com.example.agromo.model

data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val message: String? = null
)