package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.models.CityLocationResponse
import com.quranku.quranku_app.data.models.HurufHijaiyah
import com.quranku.quranku_app.data.models.PrayerTimesResponse
import com.quranku.quranku_app.data.models.hurufHijaiyahList
import com.quranku.quranku_app.data.repositorys.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _hurufHijaiyahLists = MutableStateFlow<List<HurufHijaiyah>>(emptyList())
    val hurufHijaiyahLists: StateFlow<List<HurufHijaiyah>> = _hurufHijaiyahLists.asStateFlow()

    private val _currentTime = MutableStateFlow(getCurrentTime())
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _currentDate = MutableStateFlow(getCurrentDate())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _errorMessageUser = MutableStateFlow<String?>(null)
    val errorMessageUser: StateFlow<String?> = _errorMessageUser.asStateFlow()

    private val _prayerTimes = MutableStateFlow<PrayerTimesResponse?>(null)
    val prayerTimes: StateFlow<PrayerTimesResponse?> = _prayerTimes.asStateFlow()

    private val _errorMessageTimes = MutableStateFlow<String?>(null)
    val errorMessageTimes: StateFlow<String?> = _errorMessageTimes.asStateFlow()

    private val _cityLocation = MutableStateFlow<CityLocationResponse?>(null)
    val cityLocation: StateFlow<CityLocationResponse?> = _cityLocation.asStateFlow()

    private val _errorMessageCity = MutableStateFlow<String?>(null)
    val errorMessageCity: StateFlow<String?> = _errorMessageCity.asStateFlow()

    init {
        _hurufHijaiyahLists.value = hurufHijaiyahList

        viewModelScope.launch {
            while (true) {
                _currentTime.value = getCurrentTime()

                val newDate = getCurrentDate()
                if (newDate != _currentDate.value) {
                    _currentDate.value = newDate
                    loadPrayerTimes()
                }

                delay(1000L)
            }
        }
    }

    fun fetchUserName() {
        viewModelScope.launch {
            homeRepository.fetchUserName().collect { result ->
                result.onSuccess { name ->
                    _userName.value = name
                }.onFailure { error ->
                    _errorMessageUser.value = error.message
                    delay(5000) // Wait 5 seconds before resetting error
                    resetErrorMessageUser()
                }
            }
        }
    }

    fun loadPrayerTimes() {
        viewModelScope.launch {
            homeRepository.getPrayerTimes().collect { result ->
                result.onSuccess { prayerTimes ->
                    _prayerTimes.value = prayerTimes
                }.onFailure { error ->
                    _errorMessageTimes.value = error.message
                    delay(5000) // Wait 5 seconds before resetting error
                    resetErrorMessageTimes()
                }
            }
        }
    }

    fun loadCityLocation() {
        viewModelScope.launch {
            homeRepository.getCityLocation().collect { result ->
                result.onSuccess { cityLocation ->
                    _cityLocation.value = cityLocation
                }.onFailure { error ->
                    _errorMessageCity.value = error.message
                    delay(5000) // Wait 5 seconds before resetting error
                    resetErrorMessageCity()
                }
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

    fun resetErrorMessageUser() {
        _errorMessageUser.value = null
    }

    fun resetErrorMessageTimes() {
        _errorMessageTimes.value = null
    }

    fun resetErrorMessageCity() {
        _errorMessageCity.value = null
    }

    fun resetNameTimesAndCity(){
        _userName.value = null
        _prayerTimes.value = null
        _cityLocation.value = null
    }
}