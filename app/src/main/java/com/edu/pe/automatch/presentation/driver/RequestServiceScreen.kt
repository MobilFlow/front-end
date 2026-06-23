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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.domain.model.DriverProfile
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.PrimaryDark
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private fun isoNow(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date())
}

@Composable
fun RequestServiceScreen(
    navController: NavController,
    serviceId: Long? = null,
    mechanicProfileId: Long? = null
) {
    var driverProfile by remember { mutableStateOf<DriverProfile?>(null) }
    var cars by remember { mutableStateOf<List<Car>>(emptyList()) }
    var selectedCar by remember { mutableStateOf<Car?>(null) }
    var vehicleDropdownExpanded by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var serviceTitle by remember { mutableStateOf<String?>(null) }
    var estimatedQuote by remember { mutableStateOf<Double?>(null) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val driverRepo = remember { RepositoryModule.provideDriverRepository() }
    val serviceRequestRepo = remember { RepositoryModule.provideServiceRequestRepository() }
    val catalogRepo = remember { RepositoryModule.provideServiceCatalogRepository() }

    val isFormValid = serviceId != null && driverProfile != null &&
            selectedCar != null && description.isNotBlank()

    LaunchedEffect(Unit) {
        val user = userRepo.getCurrentUser()
        if (user != null) {
            val dp = driverRepo.getDriverByUserId(user.id)
            if (dp != null) {
                driverProfile = dp
                val dbCars = driverRepo.getCarsByDriverProfile(dp.id)
                cars = dbCars
                selectedCar = dbCars.firstOrNull()
            }
        }
        if (serviceId != null) {
            val svc = catalogRepo.getAllServices().firstOrNull { it.id == serviceId }
            serviceTitle = svc?.title
            val min = svc?.minimumPrice
            val max = svc?.maximumPrice
            estimatedQuote = when {
                min != null && max != null -> (min + max) / 2.0
                min != null -> min
                max != null -> max
                else -> null
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 1,
                navType = BottomNavType.DRIVER,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.DriverHome.route) { launchSingleTop = true }
                        1 -> navController.navigate(Screen.DriverSearch.route)
                        2 -> navController.navigate(Screen.DriverHistory.route)
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
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkGray)
                }
                Text(text = "Request a Service", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGray)
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(24.dp))

                if (serviceId == null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                    ) {
                        Text(
                            "Para pedir un servicio, entra desde un servicio publicado en la pestaña Search.",
                            modifier = Modifier.padding(16.dp), color = Color(0xFF8A5300), fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                } else {
                    Text("You are requesting", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        serviceTitle ?: "Service #$serviceId",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Primary
                    )
                    estimatedQuote?.let { q ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Estimated quote", fontSize = 12.sp, color = Color.Gray)
                                    Text("Average of the mechanic's range", fontSize = 11.sp, color = Color.Gray)
                                }
                                Text("S/ ${q.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                SectionLabel("My Vehicle")
                Spacer(modifier = Modifier.height(10.dp))

                Box {
                    Card(
                        onClick = { vehicleDropdownExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryDark),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val label = selectedCar?.let { "${it.brand} ${it.model} ${it.year}" } ?: "No vehicles registered"
                            Text(text = label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                    }
                    DropdownMenu(
                        expanded = vehicleDropdownExpanded && cars.isNotEmpty(),
                        onDismissRequest = { vehicleDropdownExpanded = false }
                    ) {
                        cars.forEach { car ->
                            DropdownMenuItem(
                                text = { Text("${car.brand} ${car.model} ${car.year}") },
                                onClick = {
                                    selectedCar = car
                                    vehicleDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                if (cars.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Registra un vehículo en tu perfil antes de pedir un servicio.", color = Color(0xFFB00020), fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionLabel("What is happening to your car?")
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("It makes loud noises when I accelerate...", color = Color.Gray, fontSize = 14.sp) },
                    minLines = 3, maxLines = 5,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                        focusedBorderColor = Primary, unfocusedBorderColor = Color.Transparent, cursorColor = Primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(errorMessage!!, color = Color(0xFFB00020), fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        scope.launch {
                            isSubmitting = true
                            errorMessage = null
                            try {
                                val dp = driverProfile
                                val car = selectedCar
                                if (serviceId != null && dp != null && car != null) {
                                    serviceRequestRepo.createServiceRequest(
                                        serviceId = serviceId,
                                        driverProfileId = dp.id,
                                        mechanicProfileId = mechanicProfileId,
                                        carId = car.id,
                                        description = description.trim(),
                                        scheduledDate = isoNow()
                                    )
                                    isSubmitting = false
                                    showConfirmationDialog = true
                                } else {
                                    isSubmitting = false
                                    errorMessage = "Falta el servicio o el vehículo."
                                }
                            } catch (e: Exception) {
                                isSubmitting = false
                                errorMessage = "No se pudo enviar la solicitud: ${e.message}"
                            }
                        }
                    },
                    enabled = isFormValid && !isSubmitting,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary, contentColor = Color.White,
                        disabledContainerColor = Color.LightGray, disabledContentColor = Color.White
                    )
                ) {
                    Text(if (isSubmitting) "Submitting..." else "Send Request", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoftBackground, contentColor = DarkGray),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showConfirmationDialog) {
        ServiceRequestedDialog(
            vehicle = selectedCar?.let { "${it.brand} ${it.model} ${it.year}" } ?: "",
            description = description,
            onDismiss = {
                showConfirmationDialog = false
                navController.navigate(Screen.DriverHome.route) {
                    popUpTo(Screen.DriverHome.route) { inclusive = true }
                }
            }
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkGray)
}

@Composable
private fun ServiceRequestedDialog(
    vehicle: String,
    description: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(72.dp).clip(CircleShape).background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Primary, modifier = Modifier.size(34.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Request Sent!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                Text(
                    text = "El mecánico verá tu solicitud y podrá aceptarla o rechazarla.",
                    fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = Color.LightGray, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Vehicle", fontSize = 12.sp, color = Color.Gray)
                    Text(text = vehicle, fontSize = 13.sp, color = DarkGray, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Description", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = description, fontSize = 13.sp, color = DarkGray, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
                ) {
                    Text("Got it", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
