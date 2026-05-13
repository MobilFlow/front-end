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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                                navController.navigate(Screen.MechanicDashboard.route)
                            }
                        )
                    }

                    composable(Screen.SignUp.route) {
                        SignUp(
                            onNavigateToSignIn = {
                                navController.navigate(Screen.SignIn.route)
                            },
                            onSignUpSuccess = {
                                navController.navigate(Screen.MechanicDashboard.route)
                            }
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
                    },
                    onLoginSuccess = {
                        navController.navigate(Screen.MechanicDashboard.route)
                    }
                )
            }

            composable(Screen.SignUp.route) {
                SignUp(
                    onNavigateToSignIn = {
                        navController.navigate(Screen.SignIn.route)
                    },
                    onSignUpSuccess = {
                        navController.navigate(Screen.MechanicDashboard.route)
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