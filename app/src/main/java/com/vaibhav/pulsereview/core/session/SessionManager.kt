package com.vaibhav.pulsereview.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {

    private val _currentSession = MutableStateFlow<UserSession?>(null)
    val currentSession: StateFlow<UserSession?> = _currentSession.asStateFlow()

    val isLoggedIn: Boolean
        get() = _currentSession.value != null

    fun setSession(session: UserSession) {
        _currentSession.value = session
    }

    fun clearSession() {
        _currentSession.value = null
    }
}
