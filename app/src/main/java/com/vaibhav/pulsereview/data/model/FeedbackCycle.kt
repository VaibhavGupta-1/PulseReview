package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackCycle(
    val id: String = "",
    val companyId: String = "",
    val month: String = "",
    val status: String = ""
)
