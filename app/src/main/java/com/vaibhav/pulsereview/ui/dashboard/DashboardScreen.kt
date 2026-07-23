package com.vaibhav.pulsereview.ui.dashboard

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
import androidx.compose.material3.HorizontalDivider
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
import com.vaibhav.pulsereview.core.session.SessionManager
import com.vaibhav.pulsereview.ui.components.AppTopBar
import com.vaibhav.pulsereview.ui.components.EmptyState
import com.vaibhav.pulsereview.ui.components.LoadingIndicator
import com.vaibhav.pulsereview.ui.components.PrimaryButton
import com.vaibhav.pulsereview.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onNavigateToGiveFeedback: (revieweeId: String) -> Unit = {},
    onNavigateToFeedbackHistory: (userId: String) -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar(title = "Dashboard") }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                LoadingIndicator(modifier = Modifier.padding(innerPadding))
            }

            is UiState.Empty -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    EmptyState(
                        message = "You have no direct reports.",
                        modifier = Modifier.weight(1f)
                    )
                    ViewHistoryButton(
                        onNavigateToFeedbackHistory = onNavigateToFeedbackHistory,
                        modifier = Modifier.padding(16.dp)
                    )
                }
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
                            text = "My Reports",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(state.data) { relationship ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onNavigateToGiveFeedback(relationship.employeeId)
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = relationship.employeeName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Tap to give feedback",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        ViewHistoryButton(
                            onNavigateToFeedbackHistory = onNavigateToFeedbackHistory
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
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
private fun ViewHistoryButton(
    onNavigateToFeedbackHistory: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val session = SessionManager.currentSession.value
    if (session != null) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onNavigateToFeedbackHistory(session.userId) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "View my feedback history →",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
