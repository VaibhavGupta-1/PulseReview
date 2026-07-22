package com.vaibhav.pulsereview.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Dashboard : Route

    @Serializable
    data object HRDashboard : Route

    @Serializable
    data object FeedbackHistory : Route

    @Serializable
    data class GiveFeedback(val revieweeId: String) : Route
}
