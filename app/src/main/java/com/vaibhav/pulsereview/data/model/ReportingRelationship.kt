package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportingRelationship(
    val id: String? = null,
    @SerialName("company_id") val companyId: String,
    @SerialName("manager_id") val managerId: String,
    @SerialName("employee_id") val employeeId: String,
    @SerialName("active_from") val activeFrom: String,
    @SerialName("active_to") val activeTo: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)
