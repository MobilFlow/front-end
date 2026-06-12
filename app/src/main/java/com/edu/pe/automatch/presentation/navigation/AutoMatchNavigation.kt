// AutoMatchNavigation.kt
package com.edu.pe.automatch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.edu.pe.automatch.presentation.driver.HistoryScreen
import com.edu.pe.automatch.presentation.driver.HomeScreen
import com.edu.pe.automatch.presentation.driver.MechanicProfileScreen
import com.edu.pe.automatch.presentation.login.Login
import com.edu.pe.automatch.presentation.login.SignUp
import com.edu.pe.automatch.presentation.screens.mechanic.DriverProfileView
import com.edu.pe.automatch.presentation.screens.mechanic.EditMechanicProfile
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicDashboard
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicHistoryScreen
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicProfile
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicRequestsScreen
import com.edu.pe.automatch.presentation.driver.RequestServiceScreen
import com.edu.pe.automatch.presentation.driver.SearchScreen
import com.edu.pe.automatch.presentation.driver.UserProfileScreen

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
                //validación de rol
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
                    } else { //navegacion para conductor
                        navController.navigate(Screen.DriverHome.route)

                    }
                }
            )
        }
        //From driver to mechanic profile
        composable(
            route = "mechanic_profile/{mechanicId}",
            arguments = listOf(navArgument("mechanicId") { type = NavType.StringType })
        ) { backStackEntry ->
            MechanicProfileScreen(
                navController = navController,
                mechanicId = backStackEntry.arguments?.getString("mechanicId") ?: ""
            )
        }

        composable(Screen.DriverSearch.route) {
            SearchScreen(
                navController = navController,

            )
        }
// DRIVERS HOMESCREEN
        composable (Screen.DriverHome.route){
            HomeScreen(navController)
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
// Driver profile - mechanic
        composable(Screen.DriverProfile.route) {

            DriverProfileView()
        }
// EDIT PROFILE - mechanic
        composable(Screen.EditMechanicProfile.route) {

            EditMechanicProfile(navController)
        }
    // REQUEST SERVICE - DRIVER
        composable(Screen.RequestServiceScreen.route) {

            RequestServiceScreen(navController)
        }
    //SEARCHSCREEN - DRIVER
        composable(Screen.DriverSearch.route) {

            SearchScreen(navController)
        }

    //PROFILE - DRIVER
    composable (Screen.UserProfileScreen.route) {
        UserProfileScreen(navController)
    }
        //HISTORY - DRIVER
        composable (Screen.DriverHistory.route) {
            HistoryScreen(navController)
        }

        // Mechanic Profile Screen from Driver
        composable(
            route = Screen.MechanicProfileScreenD.route,
            arguments = listOf(navArgument("mechanicId") { type = NavType.StringType })
        ) { backStackEntry ->
            MechanicProfileScreen(
                navController = navController,
                mechanicId = backStackEntry.arguments?.getString("mechanicId") ?: ""
            )
        }
    }
}