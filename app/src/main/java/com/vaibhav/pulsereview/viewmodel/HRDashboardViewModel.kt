package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.core.session.SessionManager
import com.vaibhav.pulsereview.data.model.SubmissionStatus
import com.vaibhav.pulsereview.data.repository.FeedbackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Fetches v_submission_status for the current user's company on init.
 * Emits Empty if no reporting relationships exist for the company yet.
 *
 * NOTE: No DI framework is configured. Provide a ViewModelProvider.Factory
 * or DI module to inject dependencies at the call site.
 */
class HRDashboardViewModel(
    private val feedbackRepository: FeedbackRepository = FeedbackRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<SubmissionStatus>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<SubmissionStatus>>> = _uiState.asStateFlow()

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
                feedbackRepository.getSubmissionStatusByCompany(session.companyId)
        }
    }

    fun refresh() {
        loadSubmissionStatus()
    }
}
