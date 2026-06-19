package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.ServiceRequestInfo
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.*

@Composable
fun MechanicRequestsScreen(navController: NavController) {
    val serviceRequestRepository = remember { RepositoryModule.provideServiceRequestRepository() }
    val mechanicRepository = remember { RepositoryModule.provideMechanicRepository() }
    val userRepository = remember { RepositoryModule.provideUserRepository() }
    val driverRepository = remember { RepositoryModule.provideDriverRepository() }

    var selectedTab by remember { mutableStateOf("progress") }
    var allRequests by remember { mutableStateOf<List<ServiceRequestInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Map to cache driver names
    val driverNames = remember { mutableStateMapOf<Long, String>() }

    LaunchedEffect(Unit) {
        try {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                val mechanic = mechanicRepository.getMechanicByUserId(user.id)
                if (mechanic != null) {
                    val requests = serviceRequestRepository.getRequestsByMechanic(mechanic.id)
                    allRequests = requests
                    
                    // Fetch driver names for all requests
                    requests.forEach { req ->
                        if (!driverNames.containsKey(req.driverProfileId)) {
                            val driver = driverRepository.getDriverProfileById(req.driverProfileId)
                            driver?.let { dProfile ->
                                val u = userRepository.getUserById(dProfile.userId)
                                u?.let {
                                    driverNames[req.driverProfileId] = it.fullName
                                } ?: run {
                                    driverNames[req.driverProfileId] = "Driver ${req.driverProfileId}"
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    val filteredRequests = remember(selectedTab, allRequests) {
        if (selectedTab == "progress") {
            allRequests.filter { it.status == "SCHEDULED" || it.status == "COMPLETION_PENDING" || it.status == "PENDING" }
        } else {
            allRequests.filter { it.status == "FINALIZED" || it.status == "CANCELLED" }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(SoftBackground)
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "My Requests",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )
            Spacer(modifier = Modifier.height(20.dp))

            RequestTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (filteredRequests.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("No requests found in this category", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(filteredRequests) { request ->
                        RequestCard(
                            request = request,
                            driverName = driverNames[request.driverProfileId] ?: "Client #${request.driverProfileId}",
                            navController = navController,
                            isReview = selectedTab == "accepted"
                        )
                    }
                }
            }
        }

        BottomNavBar(
            selectedItem = 1,
            navType = BottomNavType.MECHANIC,
            onItemSelected = { index ->
                when(index) {
                    0 -> navController.navigate(Screen.MechanicDashboard.route)
                    1 -> {}
                    2 -> navController.navigate(Screen.MechanicHistory.route)
                    3 -> navController.navigate(Screen.MechanicProfile.route)
                }
            }
        )
    }
}

@Composable
private fun RequestCard(
    request: ServiceRequestInfo,
    driverName: String,
    navController: NavController,
    isReview: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = request.description?.take(25) + if ((request.description?.length ?: 0) > 25) "..." else "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark
                )
                StatusChip(request.status ?: "PENDING")
            }

            Spacer(modifier = Modifier.height(18.dp))

            DriverProfileBanner(
                name = driverName,
                vehicle = "Car ID: ${request.carId}",
                onClick = {
                    navController.navigate(Screen.DriverProfile.createRoute(request.driverProfileId))
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Scheduled Date", fontSize = 12.sp, color = Color.Gray)
                    Text(text = request.scheduledDate ?: "TBD", fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Request ID", fontSize = 12.sp, color = Color.Gray)
                    Text(text = "#${request.id}", fontWeight = FontWeight.SemiBold, color = Primary)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* Detail action */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isReview) Color.Gray else Primary)
            ) {
                Text(if (isReview) "View Details" else "Manage Request")
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val color = when(status) {
        "SCHEDULED" -> AccentBlue
        "COMPLETION_PENDING" -> Color(0xFFFFB300)
        "FINALIZED" -> Color(0xFF4CAF50)
        "PENDING" -> Color.Gray
        else -> Color.Gray
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RequestTabs(selectedTab: String, onTabSelected: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(4.dp)
    ) {
        TabButton(
            title = "In Progress",
            selected = selectedTab == "progress",
            modifier = Modifier.weight(1f)
        ) { onTabSelected("progress") }

        TabButton(
            title = "Completed/Review",
            selected = selectedTab == "accepted",
            modifier = Modifier.weight(1f)
        ) { onTabSelected("accepted") }
    }
}

@Composable
private fun TabButton(title: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Primary else Color.Transparent,
            contentColor = if (selected) Color.White else PrimaryDark
        ),
        elevation = null
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
private fun DriverProfileBanner(name: String, vehicle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftBackground)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(Primary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (name.isNotEmpty()) name.take(1) else "D", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = vehicle, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
