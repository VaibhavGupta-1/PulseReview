package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Maps to the v_score_history database view. */
@Serializable
data class ScoreHistory(
    @SerialName("reviewee_id") val revieweeId: String,
    @SerialName("parameter_name") val parameterName: String,
    val period: String,
    val score: Int,
    val comment: String? = null
)
