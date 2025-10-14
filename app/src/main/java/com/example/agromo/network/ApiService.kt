package com.example.agromo.network

import com.example.agromo.model.LoginRequest
import com.example.agromo.model.LoginResponse
import com.example.agromo.model.RegisterRequest
import com.example.agromo.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    //  LOGIN
    @POST("api/agromo/users/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    //  REGISTER
    @POST("api/agromo/users/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
}
