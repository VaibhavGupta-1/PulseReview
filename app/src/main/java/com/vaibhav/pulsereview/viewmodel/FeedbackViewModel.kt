package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.ViewModel
import com.vaibhav.pulsereview.ui.feedback.FeedbackUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedbackViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()
}
