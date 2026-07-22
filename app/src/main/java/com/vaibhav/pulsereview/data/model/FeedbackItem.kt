package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackItem(
    val id: String = "",
    val feedbackId: String = "",
    val parameterId: String = "",
    val rating: Int = 0,
    val comment: String = ""
)
