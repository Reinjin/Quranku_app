package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.data.models.PrayerTimesRequest
import com.quranku.quranku_app.data.models.PrayerTimesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager,
    private val locationRepository: LocationRepository
) {

    suspend fun fetchUserName(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getToken() // Mendapatkan token dari PreferencesManager
                val response = apiService.getProfile("Bearer $token")

                if (response.isSuccessful) {
                    val fullName = response.body()?.full_name ?: ""
                    Result.success(fullName)
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    val errorMessage = try {
                        JSONObject(errorBody).getString("message")
                    } catch (e: Exception) {
                        "Unknown error occurred"
                    }
                    Result.failure(Exception(errorMessage))
                }
            } catch (e: HttpException) {
                Result.failure(Exception("Network error: ${e.message}"))
            } catch (e: Exception) {
                Result.failure(Exception("An error occurred: ${e.message}"))
            }
        }
    }

    suspend fun getPrayerTimes(): Result<PrayerTimesResponse> {
        // Retrieve the JWT token from PreferencesManager
        val token = preferencesManager.getToken() ?: return Result.failure(Exception("Token not found"))

        // Fetch location from LocationRepository
        val locationResult = locationRepository.getCurrentLocation()
        if (locationResult.isFailure) {
            return Result.failure(locationResult.exceptionOrNull() ?: Exception("Unknown location error"))
        }

        val (latitude, longitude) = locationResult.getOrNull()!!
        val request = PrayerTimesRequest(latitude, longitude, getCurrentDate())

        // Make the API call with token and request body
        return try {
            val response: Response<PrayerTimesResponse> = apiService.getPrayerTimes("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}