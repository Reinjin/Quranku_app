package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject
import com.quranku.quranku_app.data.models.LoginRequest

class LoginRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun loginUser(email: String, password: String): Flow<Result<String>> = flow {
        try {
            val response = apiService.loginUser(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!.access_token))
            } else {
                // Parsing error body JSON untuk mengambil pesan dari "msg"
                val errorMsg = response.errorBody()?.string()?.let {
                    try {
                        JSONObject(it).getString("msg")
                    } catch (e: Exception) {
                        "Unknown error" // Pesan default jika parsing gagal
                    }
                } ?: "Unknown error"
                emit(Result.failure(Exception(errorMsg))) // Mengirim pesan error yang diambil dari JSON
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}