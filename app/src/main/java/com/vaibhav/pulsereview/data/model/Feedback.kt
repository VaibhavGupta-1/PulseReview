package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Feedback(
    val id: String = "",
    val cycleId: String = "",
    val reviewerId: String = "",
    val revieweeId: String = "",
    val status: String = ""
)
