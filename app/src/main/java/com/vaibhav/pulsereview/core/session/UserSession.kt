package com.vaibhav.pulsereview.core.session

import com.vaibhav.pulsereview.core.common.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: String,
    val companyId: String,
    val role: UserRole,
    val email: String
)
