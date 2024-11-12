package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.quranku.quranku_app.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun isLoggedIn(): Boolean {
        return preferencesManager.isLoggedIn()
    }

    fun logout() {
        preferencesManager.clearToken()
    }
}