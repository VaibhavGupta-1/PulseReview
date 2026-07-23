package com.vaibhav.pulsereview.ui.feedback

import com.vaibhav.pulsereview.data.model.FeedbackEntry

data class FeedbackUiState(
    val isLoading: Boolean = false,
    val feedbackList: List<FeedbackEntry> = emptyList(),
    val errorMessage: String? = null
)
