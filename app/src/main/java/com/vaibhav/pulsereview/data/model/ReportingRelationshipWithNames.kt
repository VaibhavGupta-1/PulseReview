package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Maps to the v_reporting_relationships_with_names database view. */
@Serializable
data class ReportingRelationshipWithNames(
    val id: String? = null,
    @SerialName("company_id") val companyId: String,
    @SerialName("manager_id") val managerId: String,
    @SerialName("manager_name") val managerName: String,
    @SerialName("employee_id") val employeeId: String,
    @SerialName("employee_name") val employeeName: String,
    @SerialName("active_from") val activeFrom: String,
    @SerialName("active_to") val activeTo: String? = null
)
