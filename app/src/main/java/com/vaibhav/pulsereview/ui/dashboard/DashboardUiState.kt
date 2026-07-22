package com.vaibhav.pulsereview.ui.dashboard

import com.vaibhav.pulsereview.data.model.User

data class DashboardUiState(
    val isLoading: Boolean = false,
    val directReports: List<User> = emptyList(),
    val errorMessage: String? = null
)
