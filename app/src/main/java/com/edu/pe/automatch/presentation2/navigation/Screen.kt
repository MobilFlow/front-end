package com.edu.pe.automatch.presentation.navigation

sealed class Screen(val route: String) {

    // AUTH
    object SignIn : Screen("signIn")
    object SignUp : Screen("signUp")

    // DRIVER FLOW
    object DriverHome : Screen("driver_home")
    object DriverSearch : Screen("driver_search")
    object DriverHistory : Screen("driver_history")
    object DriverProfile : Screen("driver_profile")

    // MECHANIC FLOW
    object MechanicDashboard : Screen("mechanic_dashboard")
    object MechanicRequests : Screen("mechanic_requests")
    object MechanicHistory : Screen("mechanic_history")
    object MechanicProfile : Screen("mechanic_profile")


    // DETAIL SCREENS
    object MechanicDetail : Screen("mechanic_detail/{mechanicId}") {
        fun createRoute(mechanicId: String) =
            "mechanic_detail/$mechanicId"
    }

    object Review : Screen("review/{serviceId}") {
        fun createRoute(serviceId: String) =
            "review/$serviceId"
    }

    object EditMechanicProfile : Screen("edit_mechanic_profile")
}