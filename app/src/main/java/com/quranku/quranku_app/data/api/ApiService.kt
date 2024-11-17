package com.quranku.quranku_app.data.api

import com.quranku.quranku_app.data.models.LoginRequest
import com.quranku.quranku_app.data.models.LoginResponse
import com.quranku.quranku_app.data.models.LogoutResponse
import com.quranku.quranku_app.data.models.PrayerTimesRequest
import com.quranku.quranku_app.data.models.PrayerTimesResponse
import com.quranku.quranku_app.data.models.ProfileResponse
import com.quranku.quranku_app.data.models.RegisterRequest
import com.quranku.quranku_app.data.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body data: RegisterRequest)
    : Response<RegisterResponse>

    @POST("/auth/login")
    suspend fun loginUser(@Body data: LoginRequest)
    : Response<LoginResponse>

    @GET("user/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @POST("/utils/prayer_times")
    suspend fun getPrayerTimes(
        @Header("Authorization") token: String,
        @Body data: PrayerTimesRequest
    ): Response<PrayerTimesResponse>

    @POST("/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>

}