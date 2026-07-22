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
            LoginScreen()
        }
        composable<Route.Dashboard> {
            DashboardScreen()
        }
        composable<Route.HRDashboard> {
            HRDashboardScreen()
        }
        composable<Route.FeedbackHistory> {
            FeedbackHistoryScreen()
        }
        composable<Route.GiveFeedback> { backStackEntry ->
            val route = backStackEntry.toRoute<Route.GiveFeedback>()
            GiveFeedbackScreen(revieweeId = route.revieweeId)
        }
    }
}
