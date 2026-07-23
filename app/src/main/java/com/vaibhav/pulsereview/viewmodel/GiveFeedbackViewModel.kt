package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.core.session.SessionManager
import com.vaibhav.pulsereview.data.model.FeedbackEntry
import com.vaibhav.pulsereview.data.model.FeedbackParameter
import com.vaibhav.pulsereview.data.repository.FeedbackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** UI-facing state combining parameters (for the form) and submission status. */
data class GiveFeedbackUiData(
    val parameters: List<FeedbackParameter>,
    val submitted: Boolean = false
)

/**
 * Loads the 5 feedback parameters on init and submits all scores as a
 * batch insert against the current open cycle.
 *
 * [revieweeId] is extracted from the navigation argument via SavedStateHandle.
 *
 * NOTE: No DI framework is configured. Provide a ViewModelProvider.Factory
 * or DI module to inject dependencies at the call site.
 */
class GiveFeedbackViewModel(
    savedStateHandle: SavedStateHandle,
    private val feedbackRepository: FeedbackRepository = FeedbackRepository()
) : ViewModel() {

    val revieweeId: String = checkNotNull(savedStateHandle["revieweeId"])

    private val _uiState =
        MutableStateFlow<UiState<GiveFeedbackUiData>>(UiState.Loading)
    val uiState: StateFlow<UiState<GiveFeedbackUiData>> = _uiState.asStateFlow()

    init {
        loadParameters()
    }

    private fun loadParameters() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when (val result = feedbackRepository.getFeedbackParameters()) {
                is UiState.Success -> {
                    _uiState.value = UiState.Success(
                        GiveFeedbackUiData(parameters = result.data)
                    )
                }
                is UiState.Error -> _uiState.value = UiState.Error(result.message)
                is UiState.Empty -> _uiState.value =
                    UiState.Error("No feedback parameters configured")
                else -> Unit
            }
        }
    }

    /**
     * Submit all parameter scores for this reviewee in a single batch.
     *
     * @param scores Map of parameterId to Pair(score, comment). Every
     *               loaded parameter must have an entry.
     */
    fun submitFeedback(scores: Map<String, Pair<Int, String?>>) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState !is UiState.Success) return@launch

            _uiState.value = UiState.Loading

            val session = SessionManager.currentSession.value
            if (session == null) {
                _uiState.value = UiState.Error("Not logged in")
                return@launch
            }

            // Find the current open cycle for this company
            when (val cyclesResult = feedbackRepository.getOpenCycles(session.companyId)) {
                is UiState.Success -> {
                    val cycle = cyclesResult.data.firstOrNull()
                    if (cycle == null) {
                        _uiState.value = UiState.Error("No open feedback cycle found")
                        return@launch
                    }

                    val entries = scores.map { (parameterId, scoreComment) ->
                        FeedbackEntry(
                            cycleId = cycle.id!!,
                            reviewerId = session.userId,
                            revieweeId = revieweeId,
                            parameterId = parameterId,
                            score = scoreComment.first,
                            comment = scoreComment.second
                        )
                    }

                    when (val submitResult = feedbackRepository.submitFeedbackEntries(entries)) {
                        is UiState.Success -> {
                            _uiState.value = UiState.Success(
                                currentState.data.copy(submitted = true)
                            )
                        }
                        is UiState.Error -> {
                            // Surface the error (e.g. duplicate submission
                            // blocked by DB constraint)
                            _uiState.value = UiState.Error(submitResult.message)
                        }
                        else -> Unit
                    }
                }
                is UiState.Error -> _uiState.value = UiState.Error(cyclesResult.message)
                is UiState.Empty -> _uiState.value =
                    UiState.Error("No open feedback cycle found")
                else -> Unit
            }
        }
    }
}
