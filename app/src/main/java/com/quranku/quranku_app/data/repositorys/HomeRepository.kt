package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.data.models.PrayerTimesRequest
import com.quranku.quranku_app.data.models.PrayerTimesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeRepository (
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager,
    private val locationRepository: LocationRepository
) {

    fun fetchUserName(): Flow<Result<String>> = flow {
        try {
            // Mendapatkan token dari PreferencesManager
            val token = preferencesManager.getToken()
            if (token == null) {
                emit(Result.failure(Exception("Token not found")))
                return@flow
            }

            // Memanggil API untuk mendapatkan profil
            val response = apiService.getProfile("Bearer $token")
            if (response.isSuccessful) {
                val fullName = response.body()?.full_name ?: ""
                emit(Result.success(fullName)) // Emit hasil sukses
            } else {
                val errorMsg = response.errorBody()?.string()?.let {
                    try {
                        JSONObject(it).getString("message")
                    } catch (e: Exception) {
                        "Sorry, Try Again Later"
                    }
                } ?: "Can't Connect to Server"
                emit(Result.failure(Exception(errorMsg))) // Emit hasil gagal
            }
        } catch (e: HttpException) {
            emit(Result.failure(Exception("Network error"))) // Emit kegagalan jaringan
        } catch (e: Exception) {
            emit(Result.failure(Exception("Can't connect to server"))) // Emit kegagalan umum
        }
    }.flowOn(Dispatchers.IO)


    fun getPrayerTimes(): Flow<Result<PrayerTimesResponse>> = flow {
        try {
            // Retrieve the JWT token from PreferencesManager
            val token = preferencesManager.getToken()
            if (token == null) {
                emit(Result.failure(Exception("Token not found")))
                return@flow
            }

            // Fetch location from LocationRepository
            val locationResult = locationRepository.getCurrentLocation()
            if (locationResult.isFailure) {
                emit(Result.failure(locationResult.exceptionOrNull() ?: Exception("Unknown location error")))
                return@flow
            }

            val (latitude, longitude) = locationResult.getOrNull()!!
            val request = PrayerTimesRequest(latitude, longitude, getCurrentDate())

            // Make the API call with token and request body
            val response: Response<PrayerTimesResponse> = apiService.getPrayerTimes("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                val errorMsg = response.errorBody()?.string()?.let {
                    try {
                        JSONObject(it).getString("message")
                    } catch (e: Exception) {
                        "Sorry, Try Again Later"
                    }
                } ?: "Can't Connect to Server"
                emit(Result.failure(Exception(errorMsg)))
            }
        } catch (e: HttpException) {
            emit(Result.failure(Exception("Network error")))
        } catch (e: Exception) {
            emit(Result.failure(Exception("Can't connect to server")))
        }
    }.flowOn(Dispatchers.IO)

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}