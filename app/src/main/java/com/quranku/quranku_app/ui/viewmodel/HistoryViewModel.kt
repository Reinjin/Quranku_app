package com.quranku.quranku_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quranku.quranku_app.data.models.HistoryItem
import com.quranku.quranku_app.data.repositorys.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _historyState = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyState: StateFlow<List<HistoryItem>> = _historyState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    // Add new state for pagination loading
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    // Add pagination states
    private var currentPage = 1
    private var isLastPage = false
    private var isLoadingPage = false

    fun fetchHistory(isFirstLoad: Boolean = false) {
        if (isFirstLoad) {
            currentPage = 1
            _historyState.value = emptyList()
            isLastPage = false
        }

        if (isLastPage || isLoadingPage) return

        viewModelScope.launch(Dispatchers.IO) {
            isLoadingPage = true
            if (currentPage == 1) {
                _loadingState.value = true
            } else {
                _isLoadingMore.value = true // Set loading more state for pagination
            }
            _errorState.value = null

            repository.fetchHistoryBelajar(currentPage, PER_PAGE).collect { result ->
                result.fold(
                    onSuccess = { response ->
                        val newItems = if (currentPage == 1) {
                            response.history_belajar
                        } else {
                            _historyState.value + response.history_belajar
                        }
                        _historyState.value = newItems

                        // Check if we've reached the last page
                        isLastPage = currentPage >= response.total_pages
                        if (!isLastPage) currentPage++
                    },
                    onFailure = { error ->
                        _errorState.value = error.message ?: "Terjadi kesalahan"
                    }
                )
                _loadingState.value = false
                _isLoadingMore.value = false // Reset loading more state
                isLoadingPage = false
            }
        }
    }

    companion object {
        const val PER_PAGE = 15
    }
}