package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.data.model.ScoreHistory
import com.vaibhav.pulsereview.data.repository.FeedbackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Fetches v_score_history for a given userId on init.
 * Emits the flat list — grouping by period is left to the UI layer.
 *
 * [userId] is extracted from the navigation argument via SavedStateHandle.
 *
 * NOTE: No DI framework is configured. Provide a ViewModelProvider.Factory
 * or DI module to inject dependencies at the call site.
 */
class FeedbackHistoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val feedbackRepository: FeedbackRepository = FeedbackRepository()
) : ViewModel() {

    val userId: String = checkNotNull(savedStateHandle["userId"])

    private val _uiState =
        MutableStateFlow<UiState<List<ScoreHistory>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ScoreHistory>>> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = feedbackRepository.getScoreHistory(userId)
        }
    }

    fun refresh() {
        loadHistory()
    }
}
