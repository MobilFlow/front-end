package com.edu.pe.automatch.presentation.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.domain.model.DriverProfile
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.PrimaryDark
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.launch

private val categories = listOf("Brakes", "Engine", "Electricity", "Suspension", "Transmission", "AC", "Tires")

@Composable
fun RequestServiceScreen(
    navController: NavController
) {
    var driverProfile by remember { mutableStateOf<DriverProfile?>(null) }
    var cars by remember { mutableStateOf<List<Car>>(emptyList()) }
    var selectedVehicle by remember { mutableStateOf("") }
    var vehicleDropdownExpanded by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val isFormValid = description.isNotBlank() && selectedCategories.isNotEmpty()

    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val driverRepo = remember { RepositoryModule.provideDriverRepository() }
    val serviceRequestRepo = remember { RepositoryModule.provideServiceRequestRepository() }

    LaunchedEffect(Unit) {
        val user = userRepo.getCurrentUser()
        if (user != null) {
            val dp = driverRepo.getDriverByUserId(user.id)
            if (dp != null) {
                driverProfile = dp
                val dbCars = driverRepo.getCarsByDriverProfile(dp.id)
                cars = dbCars
                if (dbCars.isNotEmpty()) {
                    val first = dbCars.first()
                    selectedVehicle = "${first.brand} ${first.model} ${first.year}"
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 0,
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
                            Text(
                                text = selectedVehicle.ifEmpty { "No vehicles registered" },
                                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp
                            )
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                    }
                    DropdownMenu(
                        expanded = vehicleDropdownExpanded && cars.isNotEmpty(),
                        onDismissRequest = { vehicleDropdownExpanded = false }
                    ) {
                        cars.forEach { car ->
                            val label = "${car.brand} ${car.model} ${car.year}"
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedVehicle = label
                                    vehicleDropdownExpanded = false
                                }
                            )
                        }
                    }
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

                Spacer(modifier = Modifier.height(24.dp))

                SectionLabel("Category")
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    val chunked = categories.chunked(3)
                    Column(modifier = Modifier.padding(12.dp)) {
                        chunked.forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { category ->
                                    val isSelected = category in selectedCategories
                                    Box(
                                        modifier = Modifier
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(50.dp))
                                            .background(if (isSelected) Primary else SoftBackground)
                                            .border(1.dp, if (isSelected) Primary else Color.LightGray, RoundedCornerShape(50.dp))
                                            .clickable {
                                                selectedCategories = if (isSelected) selectedCategories - category
                                                else selectedCategories + category
                                            }
                                            .padding(horizontal = 14.dp, vertical = 7.dp)
                                    ) {
                                        Text(
                                            text = category, fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        scope.launch {
                            isSubmitting = true
                            try {
                                val user = userRepo.getCurrentUser()
                                val dp = driverProfile
                                if (user != null && dp != null) {
                                    val car = cars.firstOrNull()
                                    serviceRequestRepo.createServiceRequest(
                                        serviceId = null,
                                        driverProfileId = dp.id,
                                        mechanicProfileId = null,
                                        carId = car?.id,
                                        description = description,
                                        scheduledDate = null
                                    )
                                }
                                isSubmitting = false
                                showConfirmationDialog = true
                            } catch (e: Exception) {
                                isSubmitting = false
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
                    Text(if (isSubmitting) "Submitting..." else "Book Service", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
            vehicle = selectedVehicle,
            description = description,
            categories = selectedCategories.toList(),
            onDismiss = { showConfirmationDialog = false }
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
    categories: List<String>,
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
                    CircularProgressIndicator(modifier = Modifier.size(40.dp), color = Primary, strokeWidth = 3.dp)
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = Primary, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Request Sent!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                Text(text = "Waiting for a mechanic's quote...", fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
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
                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Categories", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        categories.forEach { cat ->
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(50.dp)).background(Primary.copy(alpha = 0.1f)).padding(horizontal = 12.dp, vertical = 5.dp)
                            ) {
                                Text(text = cat, fontSize = 12.sp, color = Primary, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
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

@Preview(showBackground = true)
@Composable
fun RequestServiceScreenPreview() {
    AutoMatchTheme {
        RequestServiceScreen(rememberNavController())
    }
}
