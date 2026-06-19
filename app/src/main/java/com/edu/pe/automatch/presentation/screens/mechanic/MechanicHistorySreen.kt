package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.material3.CircularProgressIndicator
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.data.remote.dtos.ReputationSummaryDto
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.model.ServiceRequestInfo
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme

@Composable
fun MechanicHistoryScreen(
    navController: NavController
) {
    var selectedFilter by remember {
        mutableStateOf("30 Days")
    }

    val userRepository = remember {
        RepositoryModule.provideUserRepository()
    }

    val mechanicRepository = remember {
        RepositoryModule.provideMechanicRepository()
    }

    val serviceRequestRepository = remember {
        RepositoryModule.provideServiceRequestRepository()
    }

    val reviewRepository = remember {
        RepositoryModule.provideReviewRepository()
    }

    val driverRepository = remember {
        RepositoryModule.provideDriverRepository()
    }

    val driverNames = remember { mutableStateMapOf<Long, String>() }

    var historyServices by remember {
        mutableStateOf<List<ServiceRequestInfo>>(emptyList())
    }

    var isLoading by remember { mutableStateOf(true) }

    var summary by remember {
        mutableStateOf<ReputationSummaryDto?>(null)
    }

    var mechanic by remember {
        mutableStateOf<MechanicProfile?>(null)
    }

    var userProfile by remember {
        mutableStateOf<com.edu.pe.automatch.domain.model.User?>(null)
    }

    LaunchedEffect(Unit) {
        try {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                userProfile = user
                val mechanicProfile = mechanicRepository.getMechanicByUserId(user.id)
                if (mechanicProfile != null) {
                    mechanic = mechanicProfile
                    val requests = serviceRequestRepository.getRequestsByMechanic(mechanicProfile.id)
                    historyServices = requests
                    summary = reviewRepository.getReputationSummary(mechanicProfile.id)

                    // Fetch driver names for all requests
                    requests.forEach { req ->
                        if (!driverNames.containsKey(req.driverProfileId)) {
                            val driver = driverRepository.getDriverProfileById(req.driverProfileId)
                            driver?.let { dProfile ->
                                val u = userRepository.getUserById(dProfile.userId)
                                u?.let {
                                    driverNames[req.driverProfileId] = it.fullName
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

    Scaffold(
        containerColor = Color(0xFFF3F1FC),
        bottomBar = {
            BottomNavBar(
                selectedItem = 2,
                navType = BottomNavType.MECHANIC,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.MechanicDashboard.route)
                        1 -> navController.navigate(Screen.MechanicRequests.route)
                        2 -> {}
                        3 -> navController.navigate(Screen.MechanicProfile.route)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Service History",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF21195B)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // TOP CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF19154E)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "My Services",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFF332D7A))
                                    .padding(
                                        horizontal = 18.dp,
                                        vertical = 10.dp
                                    )
                            ) {
                                Text(
                                    text = "Mechanic",
                                    color = Color(0xFFBDB4FF),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            HistoryStat(
                                value = historyServices.size.toString(),
                                label = "Services"
                            )

                            HistoryStat(
                                value = summary?.totalReviews?.toString() ?: "0",
                                label = "Reviews"
                            )

                            HistoryStat(
                                value = String.format("%.1f", summary?.averageRating ?: 0.0),
                                label = "Rating",
                                highlighted = true
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // FILTERS
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val filters = listOf(
                        "Newly",
                        "15 Days",
                        "30 Days"
                    )

                    filters.forEach { filter ->
                        val isSelected = selectedFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    if (isSelected) Color(0xFF5B50D6) else Color.Transparent
                                )
                                .clickable {
                                    selectedFilter = filter
                                }
                                .padding(
                                    horizontal = 22.dp,
                                    vertical = 14.dp
                                )
                        ) {
                            Text(
                                text = filter,
                                color = if (isSelected) Color.White else Color(0xFF21195B),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Past Requests",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF21195B)
                )
            }

            item {
                if (isLoading) {
                    Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF5B50D6))
                    }
                }
            }

            items(historyServices) { service ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        // Potential navigation to details
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "Request #${service.id}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF14123B)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = service.description ?: "No description",
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = service.scheduledDate ?: "No date",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0xFFE7F7EE))
                                        .padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        )
                                ) {
                                    Text(
                                        text = service.status ?: "Unknown",
                                        color = Color(0xFF27AE60),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = driverNames[service.driverProfileId] ?: "Driver ${service.driverProfileId}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF19154E)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun HistoryStat(
    value: String,
    label: String,
    highlighted: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = if (highlighted) Color(0xFFFFD479) else Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = Color(0xFF8F88E8),
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MechanicHistoryScreenPreview() {
    AutoMatchTheme {
        MechanicHistoryScreen(
            navController = rememberNavController()
        )
    }
}
