package com.quranku.quranku_app.ui.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.models.HurufHijaiyah
import com.quranku.quranku_app.data.models.hurufHijaiyahList
import com.quranku.quranku_app.data.repositorys.HijaiyahRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailHurufViewModel @Inject constructor(
    private val hijaiyahRepository: HijaiyahRepository,
    @ApplicationContext private val context: Context // Tambahkan anotasi ApplicationContext
) : ViewModel() {

    private val _selectedHuruf = MutableStateFlow<HurufHijaiyah?>(null)
    val selectedHuruf: StateFlow<HurufHijaiyah?> = _selectedHuruf.asStateFlow()

    private val _selectedKondisi = MutableStateFlow<String>("fathah")
    val selectedKondisi: StateFlow<String> = _selectedKondisi.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordingResult = MutableStateFlow<Result<String>?>(null)
    val recordingResult: StateFlow<Result<String>?> = _recordingResult.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private val _isPlaying = MutableStateFlow<String?>(null) // Ubah ke String untuk track file yang sedang diputar
    val isPlaying: StateFlow<String?> = _isPlaying.asStateFlow()

    fun setHuruf(hurufId: Int) {
        _selectedHuruf.value = hurufHijaiyahList.find { it.id == hurufId }
    }

    fun setKondisi(kondisi: String) {
        _selectedKondisi.value = kondisi
    }

    fun startRecording() {
        viewModelScope.launch {
            try {
                val huruf = selectedHuruf.value ?: throw Exception("No huruf selected")
                val kondisi = selectedKondisi.value
                val hasilPrediksi = huruf.kondisiKelas[kondisi] ?: throw Exception("Invalid kondisi")

                _isRecording.value = true
                _recordingResult.value = hijaiyahRepository.recordAndPredictAudio(
                    huruf = huruf.huruf,
                    kondisi = kondisi,
                    hasilPrediksi = hasilPrediksi
                )
            } catch (e: Exception) {
                _recordingResult.value = Result.failure(e)
            } finally {
                _isRecording.value = false
            }
        }
    }

    fun stopRecording() {
        _isRecording.value = false
        hijaiyahRepository.stopRecording()
    }

    fun playAudio(fileName: String) {
        viewModelScope.launch {
            try {
                // Jika file yang sama sedang diputar, stop
                if (_isPlaying.value == fileName) {
                    stopAudio()
                    return@launch
                }

                // Stop audio yang sedang diputar jika ada
                stopAudio()

                // Transform filename format from "01. alif_fathah" to "alif_fathah_01"
                val transformedFileName = fileName.lowercase().let {
                    val number = it.substringBefore(".").trim() // Get "01"
                    val name = it.substringAfter(". ").trim() // Get "alif_fathah"
                    "${name}_$number" // Combine to "alif_fathah_01"
                }

                // Get resource ID
                val resourceId = context.resources.getIdentifier(
                    transformedFileName, // Already in correct format: "alif_fathah_01"
                    "raw",
                    context.packageName
                )

                if (resourceId == 0) {
                    throw Exception("Audio file not found: $transformedFileName.wav")
                }

                mediaPlayer = MediaPlayer.create(context, resourceId).apply {
                    setOnCompletionListener {
                        _isPlaying.value = null
                        stopAudio()
                    }
                    start()
                    _isPlaying.value = fileName
                }
            } catch (e: Exception) {
                _isPlaying.value = null
                // Show error message
                _recordingResult.value = Result.failure(Exception("Gagal memutar audio: ${e.message}"))
            }
        }
    }

    fun stopAudio() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
        _isPlaying.value = null
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }
}