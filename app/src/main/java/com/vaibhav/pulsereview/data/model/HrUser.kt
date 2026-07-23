package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HrUser(
    val id: String? = null,
    @SerialName("company_id") val companyId: String,
    @SerialName("user_id") val userId: String
)
