package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vaibhav.pulsereview.data.repository.FeedbackRepository

class FeedbackHistoryViewModelFactory(
    private val repository: FeedbackRepository = FeedbackRepository(),
    private val userId: String
) : ViewModelProvider.Factory {

    constructor(userId: String, repository: FeedbackRepository = FeedbackRepository()) :
            this(repository, userId)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedbackHistoryViewModel::class.java)) {
            return FeedbackHistoryViewModel(
                userId = userId,
                feedbackRepository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
