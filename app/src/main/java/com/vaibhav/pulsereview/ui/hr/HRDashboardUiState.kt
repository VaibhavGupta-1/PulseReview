package com.vaibhav.pulsereview.ui.hr

data class HRDashboardUiState(
    val isLoading: Boolean = false,
    val submissionCount: Int = 0,
    val pendingCount: Int = 0,
    val errorMessage: String? = null
)
