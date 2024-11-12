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
                val errorBody = response.errorBody()?.string()
                val errorMsg = JSONObject(errorBody ?: "{}").optString("msg", "Unknown error")
                emit(Result.failure(Exception(errorMsg)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}