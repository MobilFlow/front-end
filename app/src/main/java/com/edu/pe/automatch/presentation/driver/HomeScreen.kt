package com.edu.pe.automatch.presentation.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.*

@Composable
fun HomeScreen(
    navController: NavController
) {
    var userName by remember { mutableStateOf("") }
    var userInitials by remember { mutableStateOf("") }
    var cars by remember { mutableStateOf<List<Car>>(emptyList()) }
    var activeCount by remember { mutableStateOf(0) }
    var totalCount by remember { mutableStateOf(0) }
    var mechanics by remember { mutableStateOf<List<MechanicProfile>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()
    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val driverRepo = remember { RepositoryModule.provideDriverRepository() }
    val mechanicRepo = remember { RepositoryModule.provideMechanicRepository() }
    val serviceRequestRepo = remember { RepositoryModule.provideServiceRequestRepository() }

    LaunchedEffect(Unit) {
        try {
            val user = userRepo.getCurrentUser()
            if (user != null) {
                userName = user.fullName
                userInitials = user.fullName.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }

                val dp = driverRepo.getDriverByUserId(user.id)
                if (dp != null) {
                    cars = driverRepo.getCarsByDriverProfile(dp.id)
                    val requests = serviceRequestRepo.getRequestsByDriver(dp.id)
                    activeCount = requests.count { it.status == "PENDING" || it.status == "IN_PROGRESS" }
                    totalCount = requests.size
                }
            }

            mechanics = mechanicRepo.getAllMechanics()
        } catch (_: Exception) {
        }
        loading = false
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
                .padding(top = 40.dp)
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Hello,", color = Color.Gray, fontSize = 14.sp)
                    Text(text = userName.ifEmpty { "Loading..." }, color = DarkGray, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = userInitials.ifEmpty { "?" }, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Vehicle Dashboard Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    val car = cars.firstOrNull()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (car != null) "${car.brand} ${car.model}" else "No vehicle",
                            color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold
                        )
                        if (car != null) {
                            SuggestionChip(
                                onClick = {},
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = AccentBlue.copy(alpha = 0.2f),
                                    labelColor = Color.White
                                ),
                                border = null,
                                label = { Text(car.plate, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "Active", value = activeCount.toString(), color = AccentBlue)
                        StatItem(label = "Total", value = totalCount.toString(), color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Build,
                    title = "Request a service",
                    subtitle = "Find the best mechanic",
                    onClick = { navController.navigate(Screen.RequestServiceScreen.route) }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.History,
                    title = "View history",
                    subtitle = "$totalCount services done",
                    onClick = { navController.navigate(Screen.DriverHistory.route) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mechanics Nearby
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Mechanics Nearby", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                TextButton(onClick = { navController.navigate(Screen.DriverSearch.route) }) {
                    Text("See all", color = PrimaryLight)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(mechanics.take(10), key = { it.id.toString() }) { mechanic ->
                    MechanicCard(
                        mechanicName = mechanic.workshopName ?: "Mechanic",
                        specialties = mechanic.specialties.map { it.name },
                        onClick = {
                            navController.navigate(Screen.MechanicProfileScreenD.createRoute(mechanic.id.toString()))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Primary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkGray)
            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
private fun MechanicCard(
    mechanicName: String,
    specialties: List<String>,
    onClick: () -> Unit
) {
    val initials = mechanicName.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }
    Card(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = mechanicName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = specialties.joinToString(" • ").ifEmpty { "General" }, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AutoMatchTheme {
        HomeScreen(rememberNavController())
    }
}
