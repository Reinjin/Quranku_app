package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.data.models.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException

class ProfileRepository (
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) {
    fun fetchUserName(): Flow<Result<ProfileResponse>> = flow {
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
                val profileResponse = response.body() ?: ProfileResponse("", "")
                emit(Result.success(profileResponse)) // Emit hasil sukses
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


    fun logout(): Flow<Result<String>> = flow {
        try {
            val token = preferencesManager.getToken() // Ambil token dari preferences
            if (token.isNullOrEmpty()) {
                emit(Result.failure(Exception("Token not found")))
                return@flow
            }

            val response = apiService.logout("Bearer $token")
            if (response.isSuccessful) {
                preferencesManager.clearToken() // Hapus token dari preferences
                emit(Result.success(response.body()!!.msg))
            } else {
                val errorMsg = response.errorBody()?.string()?.let {
                    try {
                        JSONObject(it).getString("msg")
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
    }.flowOn(Dispatchers.IO) // Proses pada thread IO


}