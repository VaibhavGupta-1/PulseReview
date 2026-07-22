package com.vaibhav.pulsereview.ui.feedback

import com.vaibhav.pulsereview.data.model.Feedback

data class FeedbackUiState(
    val isLoading: Boolean = false,
    val feedbackList: List<Feedback> = emptyList(),
    val errorMessage: String? = null
)
