package com.quranku.quranku_app.data.api

import com.quranku.quranku_app.data.models.HistoryResponse
import com.quranku.quranku_app.data.models.LoginRequest
import com.quranku.quranku_app.data.models.LoginResponse
import com.quranku.quranku_app.data.models.LogoutResponse
import com.quranku.quranku_app.data.models.PrayerTimesRequest
import com.quranku.quranku_app.data.models.PrayerTimesResponse
import com.quranku.quranku_app.data.models.PredictResponse
import com.quranku.quranku_app.data.models.ProfileResponse
import com.quranku.quranku_app.data.models.RegisterRequest
import com.quranku.quranku_app.data.models.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body data: RegisterRequest)
    : Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body data: LoginRequest)
    : Response<LoginResponse>

    @GET("user/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @POST("utils/prayer_times")
    suspend fun getPrayerTimes(
        @Header("Authorization") token: String,
        @Body data: PrayerTimesRequest
    ): Response<PrayerTimesResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>

    @GET("ml/history_belajar")
    suspend fun getHistoryBelajar(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<HistoryResponse>

    @Multipart
    @POST("ml/predict_biner")
    suspend fun predictAudio(
        @Header("Authorization") token: String,
        @PartMap requestData: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part
    ): Response<PredictResponse>

}