package com.vaibhav.pulsereview.ui.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vaibhav.pulsereview.core.common.UiState
import com.vaibhav.pulsereview.data.model.ScoreHistory
import com.vaibhav.pulsereview.data.repository.FeedbackRepository
import com.vaibhav.pulsereview.ui.components.AppTopBar
import com.vaibhav.pulsereview.ui.components.LoadingIndicator
import com.vaibhav.pulsereview.ui.components.PrimaryButton
import com.vaibhav.pulsereview.viewmodel.GiveFeedbackUiData
import com.vaibhav.pulsereview.viewmodel.GiveFeedbackViewModel
import com.vaibhav.pulsereview.viewmodel.GiveFeedbackViewModelFactory

@Composable
fun GiveFeedbackScreen(
    revieweeId: String,
    onSubmitSuccess: () -> Unit = {},
    repository: FeedbackRepository = remember { FeedbackRepository() },
    viewModel: GiveFeedbackViewModel = viewModel(
        factory = GiveFeedbackViewModelFactory(repository, revieweeId)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Local form state — survives recomposition but not process death,
    // which is acceptable for a feedback form.
    val scores = remember { mutableStateMapOf<String, Int>() }
    val comments = remember { mutableStateMapOf<String, String>() }
    val submissionError = remember { androidx.compose.runtime.mutableStateOf<String?>(null) }

    // Navigate back when submitted = true
    LaunchedEffect(uiState) {
        val state = uiState
        if (state is UiState.Success && state.data.submitted) {
            onSubmitSuccess()
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Give Feedback") }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> {
                LoadingIndicator(modifier = Modifier.padding(innerPadding))
            }

            is UiState.Success -> {
                if (state.data.alreadySubmitted) {
                    AlreadySubmittedView(
                        existingScores = state.data.existingScores,
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    FeedbackForm(
                        data = state.data,
                        scores = scores,
                        comments = comments,
                        submissionError = submissionError.value,
                        onScoreChanged = { paramId, score -> scores[paramId] = score },
                        onCommentChanged = { paramId, comment -> comments[paramId] = comment },
                        onSubmit = {
                            submissionError.value = null
                            val scoreMap = state.data.parameters.associate { param ->
                                val paramId = param.id ?: return@associate "" to (0 to null as String?)
                                paramId to Pair(
                                    scores[paramId] ?: 0,
                                    comments[paramId]?.ifBlank { null }
                                )
                            }.filterKeys { it.isNotEmpty() }
                            viewModel.submitFeedback(scoreMap)
                        },
                        allScored = state.data.parameters.all { param ->
                            val score = scores[param.id]
                            score != null && score in 1..5
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            is UiState.Error -> {
                // If we had form data, show inline error so scores aren't lost
                submissionError.value = state.message
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
                        onClick = { /* ViewModel will reload on next attempt */ }
                    )
                }
            }

            is UiState.Empty, is UiState.Idle -> Unit
        }
    }
}

@Composable
private fun AlreadySubmittedView(
    existingScores: List<ScoreHistory>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Feedback Already Submitted",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Feedback for this employee has already been submitted for the current cycle.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(existingScores) { entry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
    }
}

@Composable
private fun FeedbackForm(
    data: GiveFeedbackUiData,
    scores: Map<String, Int>,
    comments: Map<String, String>,
    submissionError: String?,
    onScoreChanged: (paramId: String, score: Int) -> Unit,
    onCommentChanged: (paramId: String, comment: String) -> Unit,
    onSubmit: () -> Unit,
    allScored: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(data.parameters) { param ->
            val paramId = param.id ?: return@items

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = param.name,
                style = MaterialTheme.typography.titleSmall
            )
            if (!param.description.isNullOrBlank()) {
                Text(
                    text = param.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score selector: 5 chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (score in 1..5) {
                    val selected = scores[paramId] == score
                    FilterChip(
                        selected = selected,
                        onClick = { onScoreChanged(paramId, score) },
                        label = { Text("$score") },
                        modifier = Modifier.size(48.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = comments[paramId] ?: "",
                onValueChange = { onCommentChanged(paramId, it) },
                label = { Text("Comment (optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            if (submissionError != null) {
                Text(
                    text = submissionError,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            PrimaryButton(
                text = "Submit Feedback",
                onClick = onSubmit,
                enabled = allScored
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
