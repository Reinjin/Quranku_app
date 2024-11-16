package com.quranku.quranku_app.ui.viewmodel

import android.app.Activity
import android.content.Context
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _navigationMode = MutableStateFlow(true)
    val navigationMode: StateFlow<Boolean> = _navigationMode

    fun updateNavigationMode(context: Context) {
        if (context is Activity) {
            val windowInsets = ViewCompat.getRootWindowInsets(context.window.decorView)
            val height = windowInsets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 130
            _navigationMode.value = (height == 130)
        }
    }
}
