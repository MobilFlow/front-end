package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.edu.pe.automatch.data.remote.dtos.ReputationSummaryDto
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.model.ServiceRequestInfo
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen

// ── Colors ────────────────────────────────────────────────────────────────────
private val PurpleBg       = Color(0xFFEEEDFE)
private val DarkCardBg     = Color(0xFF1A1740)
private val DarkCardBadge  = Color(0xFF2D2A5E)
private val PurplePrimary  = Color(0xFF534AB7)
private val PurpleMid      = Color(0xFF7F77DD)
private val PurpleLight    = Color(0xFFAFA9EC)
private val PurpleDark     = Color(0xFF26215C)
private val GoldAccent     = Color(0xFFFAC775)
private val GreenBadgeBg   = Color(0xFFE1F5EE)
private val GreenBadgeText = Color(0xFF0F6E56)

@Composable
fun MechanicDashboard(navController: NavController) {
    val userRepository = remember { RepositoryModule.provideUserRepository() }
    val mechanicRepository = remember { RepositoryModule.provideMechanicRepository() }
    val serviceRequestRepository = remember { RepositoryModule.provideServiceRequestRepository() }
    val reviewRepository = remember { RepositoryModule.provideReviewRepository() }

    var userProfile by remember { mutableStateOf<com.edu.pe.automatch.domain.model.User?>(null) }
    var requests by remember { mutableStateOf<List<ServiceRequestInfo>>(emptyList()) }
    var summary by remember { mutableStateOf<ReputationSummaryDto?>(null) }

    LaunchedEffect(Unit) {
        val user = userRepository.getCurrentUser()
        if (user != null) {
            userProfile = user
            val mechanicProfile = mechanicRepository.getMechanicByUserId(user.id)
            if (mechanicProfile != null) {
                requests = serviceRequestRepository.getRequestsByMechanic(mechanicProfile.id)
                summary = reviewRepository.getReputationSummary(mechanicProfile.id)
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 0,
                navType = BottomNavType.MECHANIC,
                onItemSelected = { index ->
                    when (index) {
                        0 -> {} // Current
                        1 -> navController.navigate(Screen.MechanicRequests.route)
                        2 -> navController.navigate(Screen.MechanicHistory.route)
                        3 -> navController.navigate(Screen.MechanicProfile.route)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PurpleBg)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            HeaderRow(userProfile?.fullName ?: "Mechanic")

            StatsCard(
                requestsCount = requests.size,
                averageRating = summary?.averageRating ?: 0.0,
                totalReviews = summary?.totalReviews ?: 0
            )

            QuickActionsRow(
                navController = navController,
                activeServices = requests.count { it.status != "FINALIZED" && it.status != "CANCELLED" }
            )

            SectionWithSeeAll(title = "Recent Requests", showSeeAll = true)
            
            if (requests.isEmpty()) {
                Text("No requests found", color = Color.Gray, fontSize = 14.sp)
            } else {
                requests.take(3).forEach { request ->
                    RequestCard(request = request)
                }
            }

            SectionWithSeeAll(title = "In Progress", showSeeAll = false)
            
            val inProgress = requests.filter { it.status != "FINALIZED" && it.status != "CANCELLED" }
            if (inProgress.isEmpty()) {
                Text("No services in progress", color = Color.Gray, fontSize = 14.sp)
            } else {
                inProgress.forEach { request ->
                    InProgressCard(
                        request = request,
                        onClick = { navController.navigate(Screen.MechanicHistory.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderRow(mechanicName: String) {
    val initials = mechanicName.split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }.ifEmpty { "M" }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Welcome Back,", fontSize = 15.sp, color = PurplePrimary)
            Text(mechanicName, fontSize = 24.sp, fontWeight = FontWeight.Medium, color = PurpleDark)
        }
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(PurplePrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(initials, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
    }
}

@Composable
private fun StatsCard(requestsCount: Int, averageRating: Double, totalReviews: Int) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Services", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.White)
                Surface(shape = RoundedCornerShape(10.dp), color = DarkCardBadge) {
                    Text("Mechanic", fontSize = 13.sp, color = PurpleLight, modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                StatItem(requestsCount.toString(), "Requests", PurpleLight, Modifier.weight(1f))
                Box(Modifier.width(0.5.dp).height(44.dp).background(Color(0xFF3C3489)))
                StatItem(totalReviews.toString(), "Reviews", Color.White, Modifier.weight(1f))
                Box(Modifier.width(0.5.dp).height(44.dp).background(Color(0xFF3C3489)))
                StatItem(String.format("%.1f", averageRating), "Rating", GoldAccent, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, valueColor: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 26.sp, fontWeight = FontWeight.Medium, color = valueColor)
        Text(label, fontSize = 12.sp, color = PurpleMid)
    }
}

@Composable
private fun QuickActionsRow(navController: NavController, activeServices: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickActionCard(Icons.Outlined.Build, "New Request", "Review and accept", Modifier.weight(1f)) {
            navController.navigate(Screen.MechanicRequests.route)
        }
        QuickActionCard(Icons.Outlined.Schedule, "History", "$activeServices active", Modifier.weight(1f)) {
            navController.navigate(Screen.MechanicHistory.route)
        }
    }
}

@Composable
private fun QuickActionCard(icon: ImageVector, title: String, subtitle: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(onClick = onClick, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = modifier) {
        Column(modifier = Modifier.padding(18.dp)) {
            Icon(icon, null, tint = PurplePrimary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun SectionWithSeeAll(title: String, showSeeAll: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 17.sp, fontWeight = FontWeight.Medium, color = PurpleDark)
        if (showSeeAll) Text("See All", fontSize = 13.sp, color = PurplePrimary)
    }
}

@Composable
private fun RequestCard(request: ServiceRequestInfo) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Request #${request.id}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                StatusBadge(request.status ?: "PENDING", request.status != "FINALIZED")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(request.description ?: "No description", fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MetaChip(Icons.Outlined.Person, "Driver ${request.driverProfileId}")
                MetaChip(Icons.Outlined.Schedule, request.scheduledDate ?: "TBD")
            }
        }
    }
}

@Composable
private fun InProgressCard(request: ServiceRequestInfo, onClick: () -> Unit) {
    Card(onClick = onClick, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("In Progress #${request.id}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                StatusBadge(request.status ?: "ACTIVE", false)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("Driver ID: ${request.driverProfileId}", fontSize = 13.sp)
            Text("Scheduled: ${request.scheduledDate ?: "Not set"}", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Tap to view details", fontSize = 12.sp, color = PurplePrimary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun StatusBadge(label: String, isPurple: Boolean) {
    val bg = if (isPurple) PurpleBg else GreenBadgeBg
    val text = if (isPurple) PurplePrimary else GreenBadgeText
    Surface(shape = RoundedCornerShape(99.dp), color = bg) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = text, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
    }
}

@Composable
private fun MetaChip(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
        Text(text, fontSize = 12.sp, color = Color.Gray)
    }
}
