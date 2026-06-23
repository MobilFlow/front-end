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
import kotlinx.coroutines.launch

private val PROGRESS_STATUSES = setOf("SCHEDULED", "IN_PROGRESS", "COMPLETION_PENDING", "PENDING")

@Composable
fun MechanicRequestsScreen(navController: NavController) {
    val serviceRequestRepository = remember { RepositoryModule.provideServiceRequestRepository() }
    val mechanicRepository = remember { RepositoryModule.provideMechanicRepository() }
    val userRepository = remember { RepositoryModule.provideUserRepository() }
    val driverRepository = remember { RepositoryModule.provideDriverRepository() }
    val scope = rememberCoroutineScope()

    var selectedTab by remember { mutableStateOf("progress") }
    var allRequests by remember { mutableStateOf<List<ServiceRequestInfo>>(emptyList()) }
    var mechanicProfileId by remember { mutableStateOf<Long?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var refreshKey by remember { mutableStateOf(0) }
    var manageTarget by remember { mutableStateOf<ServiceRequestInfo?>(null) }
    var actionMessage by remember { mutableStateOf<String?>(null) }

    val driverNames = remember { mutableStateMapOf<Long, String>() }
    val carLabels = remember { mutableStateMapOf<Long, String>() }

    LaunchedEffect(refreshKey) {
        isLoading = true
        try {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                val mechanic = mechanicRepository.getMechanicByUserId(user.id)
                if (mechanic != null) {
                    mechanicProfileId = mechanic.id
                    val requests = serviceRequestRepository.getRequestsByMechanic(mechanic.id)
                    allRequests = requests

                    requests.forEach { req ->
                        // Nombre del cliente (depende de getDriverProfileById)
                        if (!driverNames.containsKey(req.driverProfileId)) {
                            val driver = driverRepository.getDriverProfileById(req.driverProfileId)
                            val name = driver?.let { userRepository.getUserById(it.userId)?.fullName }
                            driverNames[req.driverProfileId] = name ?: "Client #${req.driverProfileId}"
                        }
                        // Vehículo real (brand model · plate)
                        val carId = req.carId
                        if (carId != null && !carLabels.containsKey(carId)) {
                            val car = driverRepository.getCarsByDriverProfile(req.driverProfileId)
                                .firstOrNull { it.id == carId }
                            carLabels[carId] = car?.let { "${it.brand} ${it.model} · ${it.plate}" } ?: "Car #$carId"
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
            allRequests.filter { it.status in PROGRESS_STATUSES }
        } else {
            allRequests.filter { it.status == "FINALIZED" || it.status == "CANCELLED" }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(SoftBackground)) {
        Column(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(30.dp))
            Text("My Requests", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PrimaryDark)
            Spacer(modifier = Modifier.height(20.dp))

            RequestTabs(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

            if (actionMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(actionMessage!!, color = Color(0xFFB00020), fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = Primary) }
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
                            carLabel = request.carId?.let { carLabels[it] } ?: "Vehicle N/A",
                            isTerminal = request.status == "FINALIZED" || request.status == "CANCELLED",
                            navController = navController,
                            onManage = { manageTarget = request }
                        )
                    }
                }
            }
        }

        BottomNavBar(
            selectedItem = 1,
            navType = BottomNavType.MECHANIC,
            onItemSelected = { index ->
                when (index) {
                    0 -> navController.navigate(Screen.MechanicDashboard.route)
                    1 -> {}
                    2 -> navController.navigate(Screen.MechanicServices.route)
                    3 -> navController.navigate(Screen.MechanicProfile.route)
                }
            }
        )
    }

    manageTarget?.let { req ->
        ManageRequestDialog(
            request = req,
            onDismiss = { manageTarget = null },
            onComplete = {
                val mId = mechanicProfileId
                scope.launch {
                    actionMessage = null
                    val updated = if (mId != null)
                        serviceRequestRepository.confirmService(req.id, mId, "MECHANIC") else null
                    manageTarget = null
                    if (updated != null) refreshKey++
                    else actionMessage = "No se pudo confirmar la finalización (revisa SERVICE_DEBUG)."
                }
            },
            onCancel = {
                val mId = mechanicProfileId
                scope.launch {
                    actionMessage = null
                    val updated = if (mId != null)
                        serviceRequestRepository.cancelService(req.id, mId, "MECHANIC") else null
                    manageTarget = null
                    if (updated != null) refreshKey++
                    else actionMessage = "No se pudo cancelar (revisa SERVICE_DEBUG)."
                }
            }
        )
    }
}

@Composable
private fun ManageRequestDialog(
    request: ServiceRequestInfo,
    onDismiss: () -> Unit,
    onComplete: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Request #${request.id}", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Status: ${request.status ?: "N/A"}", fontSize = 14.sp, color = DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Puedes marcar el servicio como completado (el cliente también deberá confirmar) o cancelarlo.",
                    fontSize = 13.sp, color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onComplete,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32), contentColor = Color.White)
            ) { Text("Mark completed") }
        },
        dismissButton = {
            Row {
                TextButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
                ) { Text("Cancel request") }
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = onDismiss) { Text("Close") }
            }
        }
    )
}

@Composable
private fun RequestCard(
    request: ServiceRequestInfo,
    driverName: String,
    carLabel: String,
    isTerminal: Boolean,
    navController: NavController,
    onManage: () -> Unit
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
                    text = (request.description?.take(25) ?: "Service") + if ((request.description?.length ?: 0) > 25) "..." else "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark
                )
                StatusChip(request.status ?: "PENDING")
            }

            Spacer(modifier = Modifier.height(18.dp))

            DriverProfileBanner(
                name = driverName,
                vehicle = carLabel,
                onClick = { navController.navigate(Screen.DriverProfile.createRoute(request.driverProfileId)) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Scheduled Date", fontSize = 12.sp, color = Color.Gray)
                    Text(text = request.scheduledDate?.take(10) ?: "TBD", fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Request ID", fontSize = 12.sp, color = Color.Gray)
                    Text(text = "#${request.id}", fontWeight = FontWeight.SemiBold, color = Primary)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onManage,
                enabled = !isTerminal,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                Text(if (isTerminal) "Closed" else "Manage Request")
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val color = when (status) {
        "SCHEDULED" -> AccentBlue
        "IN_PROGRESS" -> Color(0xFF1E88E5)
        "COMPLETION_PENDING" -> Color(0xFFFFB300)
        "FINALIZED" -> Color(0xFF4CAF50)
        "CANCELLED" -> Color(0xFFD32F2F)
        else -> Color.Gray
    }
    Surface(color = color.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp)) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RequestTabs(selectedTab: String, onTabSelected: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(4.dp)) {
        TabButton("In Progress", selectedTab == "progress", Modifier.weight(1f)) { onTabSelected("progress") }
        TabButton("Completed/Cancelled", selectedTab == "accepted", Modifier.weight(1f)) { onTabSelected("accepted") }
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
                Text(text = if (name.isNotEmpty()) name.take(1).uppercase() else "C", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = vehicle, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
