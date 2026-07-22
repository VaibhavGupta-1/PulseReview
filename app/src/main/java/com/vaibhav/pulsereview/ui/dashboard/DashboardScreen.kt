package com.vaibhav.pulsereview.ui.dashboard

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vaibhav.pulsereview.ui.components.AppTopBar

/**
 * Placeholder direct report entry used to demonstrate the manager's
 * "My Reports" list and forward navigation. Will be replaced by real
 * data once repositories are wired.
 */
private data class PlaceholderReport(val id: String, val name: String)

private val placeholderReports = listOf(
    PlaceholderReport(id = "report-1", name = "Alice Johnson"),
    PlaceholderReport(id = "report-2", name = "Bob Smith"),
    PlaceholderReport(id = "report-3", name = "Carol Williams"),
)

@Composable
fun DashboardScreen(
    onNavigateToGiveFeedback: (revieweeId: String) -> Unit = {},
    onNavigateToFeedbackHistory: (userId: String) -> Unit = {},
) {
    Scaffold(
        topBar = { AppTopBar(title = "Dashboard") }
    ) { innerPadding ->
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

            items(placeholderReports) { report ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onNavigateToGiveFeedback(report.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = report.name,
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
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "View my feedback history →",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onNavigateToFeedbackHistory("self") }
                            .padding(vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
