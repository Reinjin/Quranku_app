package com.quranku.quranku_app.data.repositorys

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.api.ApiService
import com.quranku.quranku_app.data.models.PredictRequest
import com.quranku.quranku_app.ui.util.AudioRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HijaiyahRepository (
    private val apiService: ApiService,
    private val audioRecorder: AudioRecorder,
    private val preferencesManager: PreferencesManager
) {

    fun recordAndPredictAudio(
        huruf: String,
        kondisi: String,
        hasilPrediksi: String
    ): Flow<Result<String>> = flow {
        try {
            // Validate token
            val token = preferencesManager.getToken()
            if (token.isNullOrEmpty()) {
                emit(Result.failure(Exception("Token not found")))
                return@flow
            }

            // Get recorded audio data
            val audioData = audioRecorder.record()

            // Prepare date and time formats
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val currentDate = Date()

            // Buat object PredictRequest
            val predictRequest = PredictRequest(
                huruf = huruf,
                kondisi = kondisi,
                hasil_prediksi_diinginkan = hasilPrediksi,
                tanggal = dateFormat.format(currentDate),
                waktu = timeFormat.format(currentDate)
            )

            // Prepare audio part
            val audioRequestBody = audioData.toRequestBody("audio/wav".toMediaType())
            val audioPart = MultipartBody.Part.createFormData("file", "audio.wav", audioRequestBody)

            // Make API call
            val response = apiService.predictAudio(
                token = "Bearer $token",
                requestData = predictRequest.toPartMap(),
                file = audioPart
            )

            if (response.isSuccessful) {
                val result = response.body()?.result
                if (result != null) {
                    emit(Result.success(result))
                } else {
                    emit(Result.failure(Exception("Empty response from server")))
                }
            } else {
                val errorMsg = response.errorBody()?.string()?.let {
                    try {
                        JSONObject(it).getString("msg")
                    } catch (e: Exception) {
                        "Sorry, try again later"
                    }
                } ?: "Can't connect to server"
                emit(Result.failure(Exception(errorMsg)))
            }
        } catch (e: HttpException) {
            emit(Result.failure(Exception("Network error")))
        } catch (e: Exception) {
            emit(Result.failure(Exception("Can't connect to server")))
        }
    }.flowOn(Dispatchers.IO)

    fun stopRecording() {
        audioRecorder.stopRecording()
    }

}