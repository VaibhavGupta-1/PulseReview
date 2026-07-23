package com.vaibhav.pulsereview.ui.hr

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.data.model.SubmissionStatusWithNames
import com.vaibhav.pulsereview.ui.components.AppTopBar
import com.vaibhav.pulsereview.ui.components.EmptyState
import com.vaibhav.pulsereview.ui.components.LoadingIndicator
import com.vaibhav.pulsereview.ui.components.PrimaryButton
import com.vaibhav.pulsereview.viewmodel.HRDashboardViewModel

@Composable
fun HRDashboardScreen(
    onNavigateToFeedbackHistory: (userId: String) -> Unit = {},
    viewModel: HRDashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar(title = "HR Dashboard") }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                LoadingIndicator(modifier = Modifier.padding(innerPadding))
            }

            is UiState.Empty -> {
                EmptyState(
                    message = "No reporting relationships found for this company.",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is UiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Submission Status",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(state.data) { status ->
                        SubmissionStatusCard(
                            status = status,
                            onClick = {
                                onNavigateToFeedbackHistory(status.revieweeId)
                            }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }

            is UiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Retry",
                        onClick = { viewModel.refresh() }
                    )
                }
            }

            is UiState.Idle -> Unit
        }
    }
}

@Composable
private fun SubmissionStatusCard(
    status: SubmissionStatusWithNames,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reviewer: ${status.reviewerName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Reviewee: ${status.revieweeName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Period: ${status.period}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = if (status.isComplete) "✓ Complete" else "⏳ Pending",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (status.isComplete)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${status.entriesSubmitted}/${status.entriesRequired} parameters scored",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
