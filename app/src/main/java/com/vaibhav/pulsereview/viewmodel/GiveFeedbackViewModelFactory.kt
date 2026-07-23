package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vaibhav.pulsereview.data.repository.FeedbackRepository

class GiveFeedbackViewModelFactory(
    private val repository: FeedbackRepository = FeedbackRepository(),
    private val revieweeId: String
) : ViewModelProvider.Factory {

    constructor(revieweeId: String, repository: FeedbackRepository = FeedbackRepository()) :
            this(repository, revieweeId)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GiveFeedbackViewModel::class.java)) {
            return GiveFeedbackViewModel(
                revieweeId = revieweeId,
                feedbackRepository = repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
