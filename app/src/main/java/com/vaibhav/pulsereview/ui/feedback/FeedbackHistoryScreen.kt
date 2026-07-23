package com.vaibhav.pulsereview.ui.feedback

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
import com.vaibhav.pulsereview.data.model.ScoreHistory
import com.vaibhav.pulsereview.ui.components.AppTopBar
import com.vaibhav.pulsereview.ui.components.EmptyState
import com.vaibhav.pulsereview.ui.components.LoadingIndicator
import com.vaibhav.pulsereview.ui.components.PrimaryButton
import com.vaibhav.pulsereview.viewmodel.FeedbackHistoryViewModel

@Composable
fun FeedbackHistoryScreen(
    userId: String,
    viewModel: FeedbackHistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppTopBar(title = "Feedback History") }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                LoadingIndicator(modifier = Modifier.padding(innerPadding))
            }

            is UiState.Empty -> {
                EmptyState(
                    message = "No feedback history yet.",
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is UiState.Success -> {
                val grouped = state.data.groupBy { it.period }
                    .toSortedMap(compareByDescending { it })

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    grouped.forEach { (period, entries) ->
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = period,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(entries) { entry ->
                            ScoreHistoryCard(entry)
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                        }
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
private fun ScoreHistoryCard(entry: ScoreHistory) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.parameterName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${entry.score}/5",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (!entry.comment.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = entry.comment,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
