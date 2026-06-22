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
import com.edu.pe.automatch.presentation.driver.RequestServiceScreen
import com.edu.pe.automatch.presentation.driver.ReviewScreen
import com.edu.pe.automatch.presentation.driver.SearchScreen
import com.edu.pe.automatch.presentation.driver.UserProfileScreen
import com.edu.pe.automatch.presentation.login.Login
import com.edu.pe.automatch.presentation.login.SignUp
import com.edu.pe.automatch.presentation.screens.mechanic.DriverProfileView
import com.edu.pe.automatch.presentation.screens.mechanic.EditMechanicProfile
import com.edu.pe.automatch.presentation.screens.mechanic.CreateServiceScreen
import com.edu.pe.automatch.presentation.screens.mechanic.MechanicServicesScreen
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
                onLoginSuccess = { role ->
                    val destination = if (role == "ROLE_DRIVER") {
                        Screen.DriverHome.route
                    } else {
                        Screen.MechanicDashboard.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            )
        }

        // SIGN UP
        composable(Screen.SignUp.route) {

            SignUp(
                onNavigateToSignIn = {
                    navController.navigate(Screen.SignIn.route)
                },
                onSignUpSuccess = { role ->
                    val destination = if (role == "ROLE_MECHANIC") {
                        Screen.MechanicDashboard.route
                    } else {
                        Screen.DriverHome.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
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
        composable(
            route = Screen.DriverProfile.route,
            arguments = listOf(navArgument("driverId") { type = NavType.LongType })
        ) { backStackEntry ->
            val driverId = backStackEntry.arguments?.getLong("driverId") ?: 0L
            DriverProfileView(navController, driverId)
        }
// EDIT PROFILE - mechanic
        composable(Screen.EditMechanicProfile.route) {

            EditMechanicProfile(navController)
        }
        // CREATE SERVICE - mechanic
        composable(Screen.CreateService.route) {
            CreateServiceScreen(navController)
        }
        // MY SERVICES - mechanic
        composable(Screen.MechanicServices.route) {
            MechanicServicesScreen(navController)
        }
        // REQUEST SERVICE - DRIVER
        composable(Screen.RequestServiceScreen.route) {

            RequestServiceScreen(navController)
        }
        // REVIEW SCREEN
        composable(
            route = Screen.Review.route,
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            ReviewScreen(
                serviceId = backStackEntry.arguments?.getString("serviceId") ?: "",
                onPublish = {
                    navController.popBackStack()
                }
            )
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
