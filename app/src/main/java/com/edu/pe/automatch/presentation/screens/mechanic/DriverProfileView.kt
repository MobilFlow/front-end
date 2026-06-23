package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.domain.model.DriverProfile
import com.edu.pe.automatch.domain.model.ServiceRequestInfo
import com.edu.pe.automatch.domain.model.User
import com.edu.pe.automatch.presentation.components.ProfileAvatar
import com.edu.pe.automatch.presentation.theme.*

@Composable
fun DriverProfileView(navController: NavController, driverId: Long) {
    val scrollState = rememberScrollState()

    // Repositories
    val userRepository = remember { RepositoryModule.provideUserRepository() }
    val driverRepository = remember { RepositoryModule.provideDriverRepository() }
    val serviceRequestRepository = remember { RepositoryModule.provideServiceRequestRepository() }

    // State
    var driverUser by remember { mutableStateOf<User?>(null) }
    var cars by remember { mutableStateOf<List<Car>>(emptyList()) }
    var history by remember { mutableStateOf<List<ServiceRequestInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(driverId) {
        try {
            // 1. Obtener el perfil para conseguir el userId real
            val profile = driverRepository.getDriverProfileById(driverId)

            if (profile != null) {
                // 2. Con el userId del perfil, traemos los datos del usuario (Nombre, Foto, etc.)
                driverUser = userRepository.getUserById(profile.userId)
            }

            // 3. Traemos el historial usando el Profile ID
            history = serviceRequestRepository.getServiceHistory(driverId)

            // 4. Traemos los autos usando el Profile ID
            cars = driverRepository.getCarsByDriverProfile(driverId)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Primary)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SoftBackground)
                .verticalScroll(scrollState)
        ) {
            // HEADER
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp).background(PrimaryDark))
                ProfileAvatar(
                    imageUrl = driverUser?.profilePicture,
                    initials = driverUser?.fullName
                        ?.split(" ")?.filter { it.isNotBlank() }?.take(2)
                        ?.joinToString("") { it.first().uppercase() } ?: "D",
                    size = 100.dp,
                    backgroundColor = Primary,
                    contentColor = Color.White,
                    fontSize = 34.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // DRIVER INFO
            Text(
                text = driverUser?.fullName ?: "Driver Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Client Profile",
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // STATS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStat(label = "Services", value = history.size.toString())
                    ProfileStat(label = "Vehicles", value = cars.size.toString())
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // VEHICLES
            Text(
                text = "Vehicles",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (cars.isEmpty()) {
                Text(
                    "No registered vehicles",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color.Gray
                )
            } else {
                cars.forEach { car ->
                    VehicleCard(vehicle = "${car.brand} ${car.model} ${car.year}", plate = car.plate)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(28.dp))

            // HISTORY
            Text(
                text = "Service History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (history.isEmpty()) {
                Text(
                    "No service history",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color.Gray
                )
            } else {
                history.forEach { item ->
                    HistoryCard(
                        service = item.description ?: "General Service",
                        price = "Req #${item.id}",
                        date = item.scheduledDate ?: "No date"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Primary)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun VehicleCard(vehicle: String, plate: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = vehicle,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = DarkGray
                )
                Text(text = "Driver vehicle", fontSize = 13.sp, color = Color.Gray)
            }
            SuggestionChip(
                onClick = {},
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = AccentBlue.copy(alpha = 0.1f),
                    labelColor = Primary
                ),
                border = null,
                label = { Text(text = plate, fontSize = 12.sp) }
            )
        }
    }
}

@Composable
private fun HistoryCard(service: String, price: String, date: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = DarkGray
                )
                Text(text = date, fontSize = 12.sp, color = Color.Gray)
            }
            SuggestionChip(
                onClick = {},
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Primary.copy(alpha = 0.1f),
                    labelColor = Primary
                ),
                border = null,
                label = {
                    Text(text = price, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DriverProfileViewPreview() {
    AutoMatchTheme {
        DriverProfileView(rememberNavController(), 1L)
    }
}
