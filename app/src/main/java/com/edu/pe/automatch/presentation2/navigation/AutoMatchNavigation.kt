// AutoMatchNavigation.kt
package com.edu.pe.automatch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.presentation.login.Login
import com.edu.pe.automatch.presentation.login.SignUp
import com.edu.pe.automatch.presentation.screens.mechanic.DriverProfileView
import com.edu.pe.automatch.presentation.screens.mechanic.EditMechanicProfile
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicDashboard
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicHistoryScreen
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicProfile
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicRequestsScreen

@Composable
fun AutoMatchNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn.route
    ) {

        // LOGIN
        composable(Screen.SignIn.route) {

            Login(
                onNavigateToRegister = {
                    navController.navigate(Screen.SignUp.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.MechanicDashboard.route)
                }
            )
        }

        // SIGN UP
        composable(Screen.SignUp.route) {

            SignUp(
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route)
                },
                onSignUpSuccess = { isMechanic ->
                    if (isMechanic) {
                        navController.navigate(Screen.MechanicDashboard.route)
                    } else {
                        // Aquí podrías navegar al dashboard del conductor cuando esté listo
                        // navController.navigate(Screen.DriverDashboard.route)
                    }
                }
            )
        }

        // MECHANIC DASHBOARD
        composable(Screen.MechanicDashboard.route) {

            MechanicDashboard(navController)
        }

// MECHANIC HISTORY
        composable(Screen.MechanicHistory.route) {

            MechanicHistoryScreen(navController)
        }
//MECHANICS REQUESTS MANAGEMENT
        composable(Screen.MechanicRequests.route) {
            MechanicRequestsScreen(navController)
        }
// MECHANIC PROFILE
        composable(Screen.MechanicProfile.route) {

            MechanicProfile(navController)
        }
// Driver profile
        composable(Screen.DriverProfile.route) {

            DriverProfileView()
        }
// EDIT PROFILE
        composable(Screen.EditMechanicProfile.route) {

            EditMechanicProfile(navController)
        }
    }
}