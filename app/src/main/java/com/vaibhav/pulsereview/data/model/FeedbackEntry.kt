package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackEntry(
    val id: String? = null,
    @SerialName("cycle_id") val cycleId: String,
    @SerialName("reviewer_id") val reviewerId: String,
    @SerialName("reviewee_id") val revieweeId: String,
    @SerialName("parameter_id") val parameterId: String,
    val score: Int,
    val comment: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)
