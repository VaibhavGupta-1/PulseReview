package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.core.session.SessionManager
import com.vaibhav.pulsereview.data.model.ReportingRelationshipWithNames
import com.vaibhav.pulsereview.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Loads the current user's direct reports (reporting relationships where
 * they are the manager). Emits Empty when the user has no reports — this
 * is a valid, common state for employees who don't manage anyone.
 *
 * NOTE: No DI framework is configured. Provide a ViewModelProvider.Factory
 * or DI module to inject dependencies at the call site.
 */
class DashboardViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<ReportingRelationshipWithNames>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ReportingRelationshipWithNames>>> = _uiState.asStateFlow()

    init {
        loadDirectReports()
    }

    private fun loadDirectReports() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val session = SessionManager.currentSession.value
            if (session == null) {
                _uiState.value = UiState.Error("Not logged in")
                return@launch
            }

            _uiState.value = userRepository.getDirectReports(session.userId)
        }
    }

    fun refresh() {
        loadDirectReports()
    }
}
