package com.vaibhav.pulsereview.data.repository

import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.data.model.FeedbackCycle
import com.vaibhav.pulsereview.data.model.FeedbackEntry
import com.vaibhav.pulsereview.data.model.FeedbackParameter
import com.vaibhav.pulsereview.data.model.ScoreHistory
import com.vaibhav.pulsereview.data.model.SubmissionStatus
import com.vaibhav.pulsereview.data.remote.FeedbackRemoteDataSource

class FeedbackRepository(
    private val remoteDataSource: FeedbackRemoteDataSource = FeedbackRemoteDataSource()
) : BaseRepository() {

    suspend fun submitFeedbackEntry(entry: FeedbackEntry): UiState<Unit> {
        return safeApiCall { remoteDataSource.insertFeedbackEntry(entry) }
    }

    suspend fun submitFeedbackEntries(entries: List<FeedbackEntry>): UiState<Unit> {
        return safeApiCall { remoteDataSource.insertFeedbackEntries(entries) }
    }

    suspend fun getFeedbackParameters(): UiState<List<FeedbackParameter>> {
        return safeApiCallList { remoteDataSource.fetchFeedbackParameters() }
    }

    suspend fun getScoreHistory(revieweeId: String): UiState<List<ScoreHistory>> {
        return safeApiCallList { remoteDataSource.fetchScoreHistory(revieweeId) }
    }

    suspend fun getSubmissionStatusByCompany(companyId: String): UiState<List<SubmissionStatus>> {
        return safeApiCallList { remoteDataSource.fetchSubmissionStatusByCompany(companyId) }
    }

    suspend fun getSubmissionStatusByReviewer(reviewerId: String): UiState<List<SubmissionStatus>> {
        return safeApiCallList { remoteDataSource.fetchSubmissionStatusByReviewer(reviewerId) }
    }

    suspend fun getOpenCycles(companyId: String): UiState<List<FeedbackCycle>> {
        return safeApiCallList { remoteDataSource.fetchOpenCycles(companyId) }
    }
}
