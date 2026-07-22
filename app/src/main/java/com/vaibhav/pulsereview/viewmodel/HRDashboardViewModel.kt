package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.ViewModel
import com.vaibhav.pulsereview.ui.hr.HRDashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HRDashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HRDashboardUiState())
    val uiState: StateFlow<HRDashboardUiState> = _uiState.asStateFlow()
}
