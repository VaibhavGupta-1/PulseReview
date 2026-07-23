package com.vaibhav.pulsereview.data.remote

import com.vaibhav.pulsereview.core.network.SupabaseManager
import com.vaibhav.pulsereview.data.model.FeedbackCycle
import com.vaibhav.pulsereview.data.model.FeedbackEntry
import com.vaibhav.pulsereview.data.model.FeedbackParameter
import com.vaibhav.pulsereview.data.model.ScoreHistory
import com.vaibhav.pulsereview.data.model.SubmissionStatus
import io.github.jan.supabase.postgrest.from

class FeedbackRemoteDataSource {

    private val client = SupabaseManager.client

    /** Insert a single feedback entry (one parameter score + comment). */
    suspend fun insertFeedbackEntry(entry: FeedbackEntry) {
        client.from("feedback_entries").insert(entry)
    }

    /**
     * Insert multiple feedback entries in one call — used when a manager
     * submits all parameter scores for one reviewee at once.
     */
    suspend fun insertFeedbackEntries(entries: List<FeedbackEntry>) {
        client.from("feedback_entries").insert(entries)
    }

    /** Fetch all feedback parameters (the rating dimensions). */
    suspend fun fetchFeedbackParameters(): List<FeedbackParameter> {
        return client.from("feedback_parameters").select()
            .decodeList<FeedbackParameter>()
    }

    /** Fetch score history for a specific reviewee from v_score_history. */
    suspend fun fetchScoreHistory(revieweeId: String): List<ScoreHistory> {
        return client.from("v_score_history").select {
            filter { eq("reviewee_id", revieweeId) }
        }.decodeList<ScoreHistory>()
    }

    /** Fetch submission status for an entire company (HR dashboard). */
    suspend fun fetchSubmissionStatusByCompany(companyId: String): List<SubmissionStatus> {
        return client.from("v_submission_status").select {
            filter { eq("company_id", companyId) }
        }.decodeList<SubmissionStatus>()
    }

    /** Fetch submission status for a specific reviewer (manager's own view). */
    suspend fun fetchSubmissionStatusByReviewer(reviewerId: String): List<SubmissionStatus> {
        return client.from("v_submission_status").select {
            filter { eq("reviewer_id", reviewerId) }
        }.decodeList<SubmissionStatus>()
    }

    /** Fetch open feedback cycles for a company. */
    suspend fun fetchOpenCycles(companyId: String): List<FeedbackCycle> {
        return client.from("feedback_cycles").select {
            filter {
                eq("company_id", companyId)
                eq("status", "open")
            }
        }.decodeList<FeedbackCycle>()
    }
}
