package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.core.session.SessionManager
import com.vaibhav.pulsereview.data.model.SubmissionStatusWithNames
import com.vaibhav.pulsereview.data.repository.FeedbackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Fetches v_submission_status_with_names for the current user's company on init.
 * Emits Empty if no reporting relationships exist for the company yet.
 */
class HRDashboardViewModel @JvmOverloads constructor(
    private val feedbackRepository: FeedbackRepository = FeedbackRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<SubmissionStatusWithNames>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<SubmissionStatusWithNames>>> = _uiState.asStateFlow()

    init {
        loadSubmissionStatus()
    }

    private fun loadSubmissionStatus() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val session = SessionManager.currentSession.value
            if (session == null) {
                _uiState.value = UiState.Error("Not logged in")
                return@launch
            }

            _uiState.value =
                feedbackRepository.getSubmissionStatusByCompanyWithNames(session.companyId)
        }
    }

    fun refresh() {
        loadSubmissionStatus()
    }
}
