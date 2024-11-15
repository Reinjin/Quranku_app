package com.quranku.quranku_app.ui.viewmodel

import com.quranku.quranku_app.data.PreferencesManager
import com.quranku.quranku_app.data.repositorys.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val profileRepository: ProfileRepository
) {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> = _errorMessage

    fun logout() {
        preferencesManager.clearToken()
    }
}