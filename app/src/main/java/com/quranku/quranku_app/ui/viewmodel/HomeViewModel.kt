package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.models.HurufHijaiyah
import com.quranku.quranku_app.data.models.PrayerTimesResponse
import com.quranku.quranku_app.data.models.hurufHijaiyahList
import com.quranku.quranku_app.data.repositorys.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val hurufHijaiyahLists : List<HurufHijaiyah> = hurufHijaiyahList

    private val _currentTime = MutableStateFlow(getCurrentTime())
    val currentTime: StateFlow<String> = _currentTime

    private val _currentDate = MutableStateFlow(getCurrentDate())
    val currentDate: StateFlow<String> = _currentDate

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName

    private val _errorMessageUser = MutableStateFlow<String?>(null)
    val errorMessageUser: StateFlow<String?> = _errorMessageUser

    private val _prayerTimes = MutableStateFlow<PrayerTimesResponse?>(null)
    val prayerTimes: StateFlow<PrayerTimesResponse?> = _prayerTimes

    private val _errorMessageTimes = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _errorMessageTimes

    init {
        viewModelScope.launch {
            while (true) {
                val newTime = getCurrentTime()
                _currentTime.value = newTime

                // Check if the date has changed
                val newDate = getCurrentDate()
                if (newDate != _currentDate.value) {
                    _currentDate.value = newDate
                    loadPrayerTimes()
                }

                delay(1000L) // Delay 1 detik
            }
        }
    }

    fun fetchUserName() {
        viewModelScope.launch {
            val result = homeRepository.fetchUserName()
            result.onSuccess { name ->
                _userName.value = name
            }.onFailure { error ->
                _errorMessageUser.value = error.message
            }
        }
    }

    fun loadPrayerTimes() {
        viewModelScope.launch {
            val result = homeRepository.getPrayerTimes()

            if (result.isSuccess) {
                _prayerTimes.value = result.getOrNull()
            } else {
                _errorMessageTimes.value = result.exceptionOrNull()?.message
            }
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}