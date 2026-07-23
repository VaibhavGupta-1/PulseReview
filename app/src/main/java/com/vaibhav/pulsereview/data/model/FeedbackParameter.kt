package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackParameter(
    val id: String? = null,
    val name: String,
    val description: String? = null
)
