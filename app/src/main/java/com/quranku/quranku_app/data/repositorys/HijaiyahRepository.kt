package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.ui.util.AudioRecorder
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HijaiyahRepository @Inject constructor(
    private val apiService: ApiService,
    private val audioRecorder: AudioRecorder,
    private val preferencesManager: PreferencesManager
) {
    suspend fun recordAndPredictAudio(
        huruf: String,
        kondisi: String,
        hasilPrediksi: String
    ): Result<String> {
        return try {
            val token = preferencesManager.getToken() ?: throw Exception("Token not found")
            val audioData = audioRecorder.record()

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentDate = Date()

            // Convert to Map<String, RequestBody> with explicit type
            val requestMap = mapOf<String, RequestBody>(
                "huruf" to huruf.toRequestBody("text/plain".toMediaType()),
                "kondisi" to kondisi.toRequestBody("text/plain".toMediaType()),
                "hasil_prediksi_diinginkan" to hasilPrediksi.toRequestBody("text/plain".toMediaType()),
                "tanggal" to dateFormat.format(currentDate).toRequestBody("text/plain".toMediaType()),
                "waktu" to timeFormat.format(currentDate).toRequestBody("text/plain".toMediaType())
            )

            // Create audio MultipartBody.Part
            val audioRequestBody = audioData.toRequestBody("audio/wav".toMediaType())
            val audioPart = MultipartBody.Part.createFormData("file", "audio.wav", audioRequestBody)

            val response = apiService.predictAudio(
                token = "Bearer $token",
                requestData = requestMap,
                file = audioPart
            )

            if (response.isSuccessful) {
                Result.success(response.body()?.result ?: "Success")
            } else {
                val errorBody = response.errorBody()?.string()
                Result.failure(Exception(errorBody ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun stopRecording() {
        audioRecorder.stopRecording()
    }
}