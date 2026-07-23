package com.vaibhav.pulsereview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Maps to the v_submission_status_with_names database view. */
@Serializable
data class SubmissionStatusWithNames(
    @SerialName("company_id") val companyId: String,
    @SerialName("reviewer_id") val reviewerId: String,
    @SerialName("reviewer_name") val reviewerName: String,
    @SerialName("reviewee_id") val revieweeId: String,
    @SerialName("reviewee_name") val revieweeName: String,
    @SerialName("cycle_id") val cycleId: String,
    val period: String,
    @SerialName("entries_submitted") val entriesSubmitted: Long,
    @SerialName("entries_required") val entriesRequired: Long,
    @SerialName("is_complete") val isComplete: Boolean
)
