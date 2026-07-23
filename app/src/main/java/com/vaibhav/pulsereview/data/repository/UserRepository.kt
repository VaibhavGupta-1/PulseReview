package com.vaibhav.pulsereview.data.repository

import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.data.model.HrUser
import com.vaibhav.pulsereview.data.model.ReportingRelationship
import com.vaibhav.pulsereview.data.model.User
import com.vaibhav.pulsereview.data.remote.UserRemoteDataSource

class UserRepository(
    private val remoteDataSource: UserRemoteDataSource = UserRemoteDataSource()
) : BaseRepository() {

    suspend fun getUserById(userId: String): UiState<User> {
        return safeApiCall { remoteDataSource.fetchUserById(userId) }
    }

    suspend fun getDirectReports(managerId: String): UiState<List<ReportingRelationship>> {
        return safeApiCallList { remoteDataSource.fetchDirectReports(managerId) }
    }

    suspend fun getManagersFor(employeeId: String): UiState<List<ReportingRelationship>> {
        return safeApiCallList { remoteDataSource.fetchManagersFor(employeeId) }
    }

    suspend fun getHrUser(userId: String, companyId: String): UiState<HrUser?> {
        return safeApiCall { remoteDataSource.fetchHrUser(userId, companyId) }
    }
}
