package com.quranku.quranku_app.data.api

import com.quranku.quranku_app.data.models.LoginRequest
import com.quranku.quranku_app.data.models.LoginResponse
import com.quranku.quranku_app.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body data: Map<String, String>)
    : Response<RegisterResponse>

    @POST("/auth/login")
    suspend fun loginUser(@Body data: LoginRequest)
    : Response<LoginResponse>
}