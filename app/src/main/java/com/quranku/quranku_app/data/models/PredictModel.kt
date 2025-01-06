package com.quranku.quranku_app.data.models

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class PredictRequest(
    val huruf: String,
    val kondisi: String,
    val hasil_prediksi_diinginkan: String,
    val tanggal: String,
    val waktu: String
) {
    fun toPartMap(): Map<String, RequestBody> = mapOf(
        "huruf" to huruf.toRequestBody("text/plain".toMediaType()),
        "kondisi" to kondisi.toRequestBody("text/plain".toMediaType()),
        "hasil_prediksi_diinginkan" to hasil_prediksi_diinginkan.toRequestBody("text/plain".toMediaType()),
        "tanggal" to tanggal.toRequestBody("text/plain".toMediaType()),
        "waktu" to waktu.toRequestBody("text/plain".toMediaType())
    )
}

data class PredictResponse(
    val result: String
)