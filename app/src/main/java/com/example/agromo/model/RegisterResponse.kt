package com.example.agromo.model

data class RegisterResponse(
    val message: String,
    val user: User,
    val tenant: String
)

data class User(
    val id: Int,
    val username: String,
    val user_email: String
)