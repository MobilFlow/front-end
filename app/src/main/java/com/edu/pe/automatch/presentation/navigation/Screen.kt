package com.edu.pe.automatch.presentation.navigation

sealed class Screen(val route: String) {

    // AUTH
    object SignIn : Screen("signIn")
    object SignUp : Screen("signUp")

    // DRIVER FLOW
    object DriverHome : Screen("driver_home")
    object DriverSearch : Screen("driver_search")
    object DriverHistory : Screen("driver_history")
    object DriverProfile : Screen("driver_profile/{driverId}") {
        fun createRoute(driverId: Long) = "driver_profile/$driverId"
    }

    // MECHANIC FLOW
    object MechanicDashboard : Screen("mechanic_dashboard")
    object MechanicRequests : Screen("mechanic_requests")
    object MechanicHistory : Screen("mechanic_history")

    //DRIVER FLOW
    object UserProfileScreen : Screen("user_profile_screen")
    object MechanicProfile : Screen("mechanic_profile/{mechanicId}")

    // DETAIL SCREENS
    object MechanicDetail : Screen("mechanic_detail/{mechanicId}") {
        fun createRoute(mechanicId: String) =
            "mechanic_detail/$mechanicId"
    }

    object Review : Screen("review/{serviceId}") {
        fun createRoute(serviceId: String) =
            "review/$serviceId"
    }

    //From Driver to Mechanic Profile
    object MechanicProfileScreenD : Screen("mechanic_profile_driver/{mechanicId}") {
        fun createRoute(mechanicId: String) = "mechanic_profile_driver/$mechanicId"
    }

    object RequestServiceScreen : Screen("request_service?mechanicId={mechanicId}")

    object EditMechanicProfile : Screen("edit_mechanic_profile")

    object CreateService : Screen("create_service")

    object MechanicServices : Screen("mechanic_services")
}