package com.vaibhav.pulsereview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.core.common.UserRole
import com.vaibhav.pulsereview.core.network.SupabaseManager
import com.vaibhav.pulsereview.core.session.SessionManager
import com.vaibhav.pulsereview.core.session.UserSession
import com.vaibhav.pulsereview.data.model.User
import com.vaibhav.pulsereview.data.repository.UserRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Handles Supabase Auth sign-in, then fetches the matching users row
 * and populates SessionManager on success.
 *
 * NOTE: No DI framework is configured. Provide a ViewModelProvider.Factory
 * or DI module to inject dependencies at the call site.
 */
class LoginViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val uiState: StateFlow<UiState<User>> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                // Authenticate with Supabase Auth
                SupabaseManager.client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                // Retrieve the authenticated user's id
                val authUser = SupabaseManager.client.auth.currentUserOrNull()
                if (authUser == null) {
                    _uiState.value = UiState.Error("Authentication succeeded but no user session found")
                    return@launch
                }

                // Fetch the matching row from the users table
                when (val result = userRepository.getUserById(authUser.id)) {
                    is UiState.Success -> {
                        val user = result.data

                        // Check if this user is HR
                        val hrResult = userRepository.getHrUser(user.id, user.companyId)
                        val isHr = hrResult is UiState.Success && hrResult.data != null

                        // Check if this user manages anyone
                        val reportsResult = userRepository.getDirectReports(user.id)
                        val isManager = reportsResult is UiState.Success &&
                                reportsResult.data.isNotEmpty()

                        val role = when {
                            isHr -> UserRole.HR
                            isManager -> UserRole.MANAGER
                            else -> UserRole.EMPLOYEE
                        }

                        SessionManager.setSession(
                            UserSession(
                                userId = user.id,
                                companyId = user.companyId,
                                role = role,
                                email = user.email
                            )
                        )

                        _uiState.value = UiState.Success(user)
                    }
                    is UiState.Error -> {
                        _uiState.value = UiState.Error(result.message)
                    }
                    else -> {
                        _uiState.value = UiState.Error("User profile not found")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.message ?: "Sign-in failed"
                )
            }
        }
    }
}
