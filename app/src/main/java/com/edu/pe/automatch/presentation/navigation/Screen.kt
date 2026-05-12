package com.edu.pe.automatch.presentation.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("signIn")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object UserProfile : Screen("user_profile")
    object MechanicProfile : Screen("mechanic_profile/{mechanicId}") {
        fun createRoute(mechanicId: String) = "mechanic_profile/$mechanicId"
    }
    object Review : Screen("review/{serviceId}") {
        fun createRoute(serviceId: String) = "review/$serviceId"
    }
}