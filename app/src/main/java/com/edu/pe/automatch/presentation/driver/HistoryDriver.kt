package com.edu.pe.automatch.presentation.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.ServiceRequestInfo
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.launch

private val ACTIVE_STATUSES = setOf("SCHEDULED", "IN_PROGRESS", "COMPLETION_PENDING", "PENDING")

@Composable
fun HistoryScreen(
    navController: NavController
) {
    var requests by remember { mutableStateOf<List<ServiceRequestInfo>>(emptyList()) }
    var driverProfileId by remember { mutableStateOf<Long?>(null) }
    var loading by remember { mutableStateOf(true) }
    var refreshKey by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val driverRepo = remember { RepositoryModule.provideDriverRepository() }
    val serviceRequestRepo = remember { RepositoryModule.provideServiceRequestRepository() }

    LaunchedEffect(refreshKey) {
        loading = true
        try {
            val user = userRepo.getCurrentUser()
            if (user != null) {
                val dp = driverRepo.getDriverByUserId(user.id)
                if (dp != null) {
                    driverProfileId = dp.id
                    requests = serviceRequestRepo.getRequestsByDriver(dp.id)
                }
            }
        } catch (_: Exception) {
        }
        loading = false
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 2,
                navType = BottomNavType.DRIVER,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.DriverHome.route) { launchSingleTop = true }
                        1 -> navController.navigate(Screen.DriverSearch.route)
                        2 -> { }
                        3 -> navController.navigate(Screen.UserProfileScreen.route)
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SoftBackground)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "My Requests",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            if (message != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(message!!, color = Color(0xFFB00020), fontSize = 13.sp, modifier = Modifier.padding(horizontal = 20.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
                ) {
                    items(requests, key = { it.id }) { req ->
                        RequestHistoryCard(
                            request = req,
                            onConfirm = {
                                val dpId = driverProfileId
                                scope.launch {
                                    message = null
                                    val updated = if (dpId != null)
                                        serviceRequestRepo.confirmService(req.id, dpId, "DRIVER") else null
                                    if (updated != null) {
                                        navController.navigate(Screen.Review.createRoute(req.id.toString()))
                                    } else {
                                        message = "No se pudo confirmar (revisa SERVICE_DEBUG)."
                                    }
                                }
                            },
                            onDelete = {
                                val dpId = driverProfileId
                                scope.launch {
                                    message = null
                                    val updated = if (dpId != null)
                                        serviceRequestRepo.cancelService(req.id, dpId, "DRIVER") else null
                                    if (updated != null) refreshKey++
                                    else message = "No se pudo eliminar (revisa SERVICE_DEBUG)."
                                }
                            },
                            onReview = {
                                navController.navigate(Screen.Review.createRoute(req.id.toString()))
                            }
                        )
                    }
                    if (requests.isEmpty()) {
                        item {
                            Text(
                                "No requests yet",
                                color = Color.Gray, fontSize = 14.sp,
                                modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }
            }
        }
    }
}

@Composable
private fun RequestHistoryCard(
    request: ServiceRequestInfo,
    onConfirm: () -> Unit,
    onDelete: () -> Unit,
    onReview: () -> Unit
) {
    val status = request.status ?: "PENDING"
    val isActive = status in ACTIVE_STATUSES
    val isFinalized = status == "FINALIZED"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = request.description ?: "Service #${request.id}",
                    fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkGray,
                    modifier = Modifier.weight(1f)
                )
                StatusPill(status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = request.scheduledDate?.take(10) ?: "", fontSize = 12.sp, color = Color.Gray)

            if (isActive) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
                    ) { Text("Delete", fontSize = 13.sp) }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32), contentColor = Color.White)
                    ) { Text("Confirm done", fontSize = 13.sp) }
                }
            } else if (isFinalized) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onReview,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
                ) { Text("Leave a review", fontSize = 13.sp) }
            }
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    val color = when (status) {
        "SCHEDULED" -> Color(0xFF1565C0)
        "IN_PROGRESS" -> Color(0xFF1E88E5)
        "COMPLETION_PENDING" -> Color(0xFFFFB300)
        "FINALIZED" -> Color(0xFF2E7D32)
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

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    AutoMatchTheme {
        HistoryScreen(rememberNavController())
    }
}
