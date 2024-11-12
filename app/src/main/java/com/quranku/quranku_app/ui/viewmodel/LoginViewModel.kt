package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.repositorys.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginResponse = MutableStateFlow<String?>(null)
    val loginResponse: StateFlow<String?> = _loginResponse

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loadingState.value = true
            loginRepository.loginUser(email, password).collect { result ->
                result.onSuccess { token ->
                    saveToken(token) // Simpan token menggunakan PreferencesManager
                    _loginResponse.value = "Login Successfully"
                }.onFailure { error ->
                    _loginResponse.value = error.message
                }
                _loadingState.value = false
            }
        }
    }

    private fun saveToken(token: String) {
        preferencesManager.saveToken(token) // Simpan token dengan PreferencesManager
    }

    fun resetLoginResponse() {
        _loginResponse.value = null
    }
}

