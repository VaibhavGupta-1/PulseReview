package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportingRelationship(
    val id: String = "",
    val managerId: String = "",
    val reportId: String = ""
)
