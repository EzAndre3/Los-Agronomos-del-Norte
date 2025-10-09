package com.example.agromo.model

data class LoginResponse(
    val token: String?, // o el campo que devuelva la API
    val user: UserData? // opcional, según el JSON real
)

data class UserData(
    val id: Int?,
    val name: String?,
    val email: String?
)
