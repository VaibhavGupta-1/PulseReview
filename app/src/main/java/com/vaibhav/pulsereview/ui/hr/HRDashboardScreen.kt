package com.vaibhav.pulsereview.ui.hr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vaibhav.pulsereview.ui.components.AppTopBar

@Composable
fun HRDashboardScreen() {
    Scaffold(
        topBar = { AppTopBar(title = "HR Dashboard") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "HR Dashboard",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
