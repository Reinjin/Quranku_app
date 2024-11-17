package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.repositorys.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _errorMessageLogout = MutableStateFlow<String?>(null)
    val errorMessageLogout : StateFlow<String?> = _errorMessageLogout

    private val _logoutState = MutableStateFlow<String?>(null)
    val logoutState: StateFlow<String?> = _logoutState

    fun fetchUserProfile() {
        viewModelScope.launch {
            profileRepository.fetchUserName().collect { result ->
                result.onSuccess { result ->
                    _userName.value = result.full_name
                    _email.value = result.email
                }.onFailure { error ->
                    _errorMessage.value = error.message
                    delay(5000)
                    resetErrorMessage()
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loadingState.value = true
            profileRepository.logout().collect { result ->
                result.onSuccess { result ->
                    _logoutState.value = result
                }.onFailure { error ->
                    _errorMessageLogout.value = error.message
                    delay(5000)
                    resetErrorMessageLogout()
                }
            }
            _loadingState.value = false
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }

    fun resetErrorMessageLogout() {
        _errorMessageLogout.value = null
    }

}