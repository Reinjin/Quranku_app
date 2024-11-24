package com.quranku.quranku_app.ui.viewmodel

import android.annotation.SuppressLint
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
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _selectedHuruf = MutableStateFlow<HurufHijaiyah?>(null)
    val selectedHuruf: StateFlow<HurufHijaiyah?> = _selectedHuruf.asStateFlow()

    private val _selectedKondisi = MutableStateFlow<String>("fathah")
    val selectedKondisi: StateFlow<String> = _selectedKondisi.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordingResult = MutableStateFlow<Result<String>?>(null)
    val recordingResult: StateFlow<Result<String>?> = _recordingResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private val _isPlaying = MutableStateFlow<String?>(null)
    val isPlaying: StateFlow<String?> = _isPlaying.asStateFlow()

    fun setHuruf(hurufId: Int) {
        _selectedHuruf.value = hurufHijaiyahList.find { it.id == hurufId }
    }

    fun setKondisi(kondisi: String) {
        _selectedKondisi.value = kondisi
    }

    private val _showResultSheet = MutableStateFlow(false)
    val showResultSheet: StateFlow<Boolean> = _showResultSheet.asStateFlow()

    fun startRecording() {
        val huruf = selectedHuruf.value ?: run {
            _recordingResult.value = Result.failure(Exception("No huruf selected"))
            return
        }
        val kondisi = selectedKondisi.value
        val hasilPrediksi = huruf.kondisiKelas[kondisi] ?: run {
            _recordingResult.value = Result.failure(Exception("Invalid kondisi"))
            return
        }

        viewModelScope.launch {
            try {
                _isRecording.value = true
                _recordingResult.value = null

                hijaiyahRepository.recordAndPredictAudio(
                    huruf = huruf.huruf,
                    kondisi = kondisi,
                    hasilPrediksi = hasilPrediksi
                ).collect { result ->
                    _recordingResult.value = result
                    _isRecording.value = false
                    _isLoading.value = false
                    _showResultSheet.value = true  // Tampilkan sheet setelah hasil tersedia
                }
            } catch (e: Exception) {
                _recordingResult.value = Result.failure(e)
                _isRecording.value = false
                _isLoading.value = false
                _showResultSheet.value = true
            }
        }
    }

    fun stopRecording() {
        if (_isRecording.value) {
            hijaiyahRepository.stopRecording()
            _isRecording.value = false
            _isLoading.value = true
        }
    }

    fun hideResultSheet() {
        _showResultSheet.value = false
        _recordingResult.value = null
    }

    @SuppressLint("DiscouragedApi")
    fun playAudio(fileName: String) {
        viewModelScope.launch {
            try {
                // If the same file is playing, stop it
                if (_isPlaying.value == fileName) {
                    stopAudio()
                    return@launch
                }

                // Stop any currently playing audio
                stopAudio()

                // Transform filename format from "01. alif_fathah" to "alif_fathah_01"
                val transformedFileName = fileName.lowercase().let {
                    val number = it.substringBefore(".").trim()
                    val name = it.substringAfter(". ").trim()
                    "${name}_$number"
                }

                // Get resource ID
                val resourceId = context.resources.getIdentifier(
                    transformedFileName,
                    "raw",
                    context.packageName
                )

                if (resourceId == 0) {
                    throw Exception("Audio file not found: $transformedFileName")
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
                _recordingResult.value = Result.failure(Exception("Failed to play audio: ${e.message}"))
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