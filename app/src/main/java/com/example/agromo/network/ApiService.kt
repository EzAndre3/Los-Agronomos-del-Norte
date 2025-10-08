/*package com.example.agromo.network

import com.example.agromo.model.LoginRequest
import com.example.agromo.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("{tenant}/users/login")
    suspend fun login(
        @Path("tenant") tenant: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>
}
*/