package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackCycle(
    val id: String? = null,
    @SerialName("company_id") val companyId: String,
    val period: String,
    val status: String = "open",
    @SerialName("created_at") val createdAt: String? = null
)
