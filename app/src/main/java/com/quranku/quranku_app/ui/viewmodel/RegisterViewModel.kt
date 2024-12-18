package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.repositorys.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _registerResponse = MutableStateFlow<String?>(null)
    val registerResponse: StateFlow<String?> = _registerResponse.asStateFlow()

    // Tambahkan loadingState
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    fun registerUser(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _loadingState.value = true // Mulai loading
            registerRepository.registerUser(fullName, email, password).collect { result ->
                result.onSuccess { message ->
                    _registerResponse.value = message // Menyimpan pesan sukses
                }.onFailure { error ->
                    _registerResponse.value = error.message // Menyimpan pesan error
                }
                _loadingState.value = false // Selesai loading
            }
        }
    }

    // Reset setelah Toast ditampilkan
    fun resetRegisterResponse() {
        _registerResponse.value = null
    }
}