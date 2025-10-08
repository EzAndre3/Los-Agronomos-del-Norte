/*package com.example.agromo.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://api.ecoranger.org/"
    private const val TENANT = "agromo"
    private const val API_KEY = "agromo-key-123"

    // 🔹 Cliente HTTP básico con header x-api-key
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader("x-api-key", API_KEY)
                .build()
            chain.proceed(request)
        }
        .build()

    // 🔹 Retrofit
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun getTenant() = TENANT
}
*/