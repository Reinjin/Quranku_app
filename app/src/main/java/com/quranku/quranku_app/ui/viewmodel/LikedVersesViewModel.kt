package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.models.LikedVerse
import com.quranku.quranku_app.data.repositorys.LikedVerseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedVersesViewModel @Inject constructor(
    private val likedVerseRepository: LikedVerseRepository
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    private val _likedVerses = MutableStateFlow<List<LikedVerse>>(emptyList())
    val likedVerses = _likedVerses.asStateFlow()

    init {
        viewModelScope.launch {
            likedVerseRepository.getAllLikedVerses()
                .catch { error ->
                    _errorState.value = error.message
                }
                .collect { verses ->
                    _likedVerses.value = verses
                }
        }
    }

    fun deletedVerseLiked(surahId : Int, verseId: Int){
        viewModelScope.launch{
            likedVerseRepository.deleteLikeVerse(surahId, verseId)
        }

    }
}