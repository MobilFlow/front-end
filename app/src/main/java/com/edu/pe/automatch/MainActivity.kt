package com.edu.pe.automatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.edu.pe.automatch.presentation.driver.HomeScreen
import com.edu.pe.automatch.presentation.driver.MechanicProfileScreen
import com.edu.pe.automatch.presentation.driver.ReviewScreen
import com.edu.pe.automatch.presentation.driver.UserProfileScreen
import com.edu.pe.automatch.presentation.login.Login
import com.edu.pe.automatch.presentation.login.SignUp
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutoMatchTheme() {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.SignIn.route
                ) {

                    composable(Screen.SignIn.route) {
                        Login(
                            onNavigateToRegister = {
                                navController.navigate(Screen.SignUp.route)
                            },
                            onLoginSuccess = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.SignIn.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.SignUp.route) {
                        SignUp(onNavigateToSignIn = {
                            navController.navigate(Screen.SignIn.route)
                        })
                    }

                    composable(Screen.Home.route) {
                        HomeScreen(
                            onNavigateToProfile = {
                                navController.navigate(Screen.UserProfile.route)
                            },
                            onNavigateToMechanic = { mechanicId ->
                                navController.navigate(Screen.MechanicProfile.createRoute(mechanicId))
                            },
                            onNavigateToReview = { serviceId ->
                                navController.navigate(Screen.Review.createRoute(serviceId))
                            }
                        )
                    }

                    composable(Screen.UserProfile.route) {
                        UserProfileScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = Screen.MechanicProfile.route,
                        arguments = listOf(navArgument("mechanicId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val mechanicId = backStackEntry.arguments?.getString("mechanicId") ?: ""
                        MechanicProfileScreen(
                            mechanicId = mechanicId,
                            onNavigateBack = { navController.popBackStack() },
                            onRequestService = {
                                navController.navigate(Screen.Review.createRoute("1"))
                            }
                        )
                    }

                    composable(
                        route = Screen.Review.route,
                        arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val serviceId = backStackEntry.arguments?.getString("serviceId") ?: ""
                        ReviewScreen(
                            serviceId = serviceId,
                            onPublish = { navController.popBackStack() }
                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {

    AutoMatchTheme {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.SignIn.route
        ) {

            composable(Screen.SignIn.route) {
                Login(
                    onNavigateToRegister = {
                        navController.navigate(Screen.SignUp.route)
                    }
                )
            }

            composable(Screen.SignUp.route) {
                SignUp(
                    onNavigateToSignIn = {
                        navController.navigate(Screen.SignIn.route)
                    }
                )
            }
        }
    }
}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutoMatchTheme {
        Greeting("Android")
    }
}*/