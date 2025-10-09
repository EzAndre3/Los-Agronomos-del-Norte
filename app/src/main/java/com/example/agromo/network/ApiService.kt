package com.example.agromo.network

import com.example.agromo.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/agromo/users/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<Unit> // Cambia a LoginResponse si tienes el modelo
}
