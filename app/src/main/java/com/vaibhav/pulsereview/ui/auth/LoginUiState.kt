package com.vaibhav.pulsereview.ui.auth

data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val errorMessage: String? = null
)
