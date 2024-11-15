package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.data.models.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) {
    suspend fun fetchUserName(): Result<ProfileResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val token = preferencesManager.getToken() // Mendapatkan token dari PreferencesManager
                val response = apiService.getProfile("Bearer $token")

                if (response.isSuccessful) {
                    val profileResponse = response.body() ?: ProfileResponse("", "")
                    Result.success(profileResponse)
                } else {
                    val errorMsg = response.errorBody()?.string()?.let {
                        try {
                            JSONObject(it).getString("message")
                        }catch (e: Exception){
                            "Sorry, Try Again Later"
                        }
                    } ?: "Can't Connect to Server"
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: HttpException) {
                Result.failure(Exception("Network error"))
            } catch (e: Exception) {
                Result.failure(Exception("Can't connect to server"))
            }
        }
    }

}