package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.data.models.HistoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class HistoryRepository (
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager // Inject PreferencesManager
) {

    fun fetchHistoryBelajar(
        page: Int,
        perPage: Int
    ): Flow<Result<HistoryResponse>> = flow {
        try {
            // Ambil token dari PreferencesManager
            val token = preferencesManager.getToken()
            if (token.isNullOrEmpty()) {
                emit(Result.failure(Exception("Token not found")))
                return@flow
            }

            // Panggil API untuk mendapatkan data history belajar
            val response = apiService.getHistoryBelajar(
                token = "Bearer $token",
                page = page,
                perPage = perPage
            )

            if (response.isSuccessful) {
                val historyResponse = response.body() ?: HistoryResponse(
                    history_belajar = emptyList(),
                    current_page = 0,
                    total_items = 0,
                    total_pages = 0,
                    per_page = 0
                )
                emit(Result.success(historyResponse)) // Emit sukses
            } else {
                // Parsing error message dari body response jika ada
                val errorMsg = response.errorBody()?.string()?.let {
                    try {
                        JSONObject(it).getString("msg")
                    } catch (e: Exception) {
                        "Sorry, Try Again Later"
                    }
                } ?: "Can't Connect to Server"
                emit(Result.failure(Exception(errorMsg))) // Emit kegagalan
            }
        } catch (e: HttpException) {
            emit(Result.failure(Exception("Network error"))) // Emit kegagalan jaringan
        } catch (e: Exception) {
            emit(Result.failure(Exception("Can't connect to server"))) // Emit kegagalan umum
        }
    }.flowOn(Dispatchers.IO)
}