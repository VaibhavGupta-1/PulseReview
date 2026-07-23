package com.vaibhav.pulsereview.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.vaibhav.pulsereview.ui.auth.LoginScreen
import com.vaibhav.pulsereview.ui.auth.SplashScreen
import com.vaibhav.pulsereview.ui.dashboard.DashboardScreen
import com.vaibhav.pulsereview.ui.feedback.FeedbackHistoryScreen
import com.vaibhav.pulsereview.ui.feedback.GiveFeedbackScreen
import com.vaibhav.pulsereview.ui.hr.HRDashboardScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash
    ) {
        composable<Route.Splash> {
            SplashScreen()
        }
        composable<Route.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Route.Dashboard) {
                        popUpTo<Route.Login> { inclusive = true }
                    }
                }
            )
        }
        composable<Route.Dashboard> {
            DashboardScreen(
                onNavigateToGiveFeedback = { revieweeId ->
                    navController.navigate(Route.GiveFeedback(revieweeId))
                },
                onNavigateToFeedbackHistory = { userId ->
                    navController.navigate(Route.FeedbackHistory(userId))
                }
            )
        }
        composable<Route.HRDashboard> {
            HRDashboardScreen(
                onNavigateToFeedbackHistory = { userId ->
                    navController.navigate(Route.FeedbackHistory(userId))
                }
            )
        }
        composable<Route.FeedbackHistory> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.FeedbackHistory>()
            FeedbackHistoryScreen(userId = route.userId)
        }
        composable<Route.GiveFeedback> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.GiveFeedback>()
            GiveFeedbackScreen(
                revieweeId = route.revieweeId,
                onSubmitSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}
