package com.edu.pe.automatch.presentation.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("signIn")
    object SignUp : Screen("signup")
    object MechanicDashboard : Screen("mechanicDashboard")
    object MechanicProfile : Screen("mechanicProfile")
    object EditMechanicProfile : Screen("editMechanicProfile")
}