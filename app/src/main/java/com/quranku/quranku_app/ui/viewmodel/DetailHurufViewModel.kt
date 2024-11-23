package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.models.HurufHijaiyah
import com.quranku.quranku_app.data.models.hurufHijaiyahList
import com.quranku.quranku_app.data.repositorys.HijaiyahRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailHurufViewModel @Inject constructor(
    private val hijaiyahRepository: HijaiyahRepository
) : ViewModel() {

    private val _selectedHuruf = MutableStateFlow<HurufHijaiyah?>(null)
    val selectedHuruf: StateFlow<HurufHijaiyah?> = _selectedHuruf.asStateFlow()

    private val _selectedKondisi = MutableStateFlow<String>("fathah")
    val selectedKondisi: StateFlow<String> = _selectedKondisi.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordingResult = MutableStateFlow<Result<String>?>(null)
    val recordingResult: StateFlow<Result<String>?> = _recordingResult.asStateFlow()

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
}