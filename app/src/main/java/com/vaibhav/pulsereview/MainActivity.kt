package com.vaibhav.pulsereview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vaibhav.pulsereview.ui.navigation.AppNavigation
import com.vaibhav.pulsereview.ui.theme.PulseReviewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PulseReviewTheme {
                AppNavigation()
            }
        }
    }
}