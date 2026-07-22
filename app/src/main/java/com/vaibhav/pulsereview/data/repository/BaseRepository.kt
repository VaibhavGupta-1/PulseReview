package com.vaibhav.pulsereview.data.repository

import com.vaibhav.pulsereview.core.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): UiState<T> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiCall()
                UiState.Success(result)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    protected suspend fun <T> safeApiCallList(
        apiCall: suspend () -> List<T>
    ): UiState<List<T>> {
        return withContext(Dispatchers.IO) {
            try {
                val result = apiCall()
                if (result.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Success(result)
                }
            } catch (e: Exception) {
                UiState.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}
