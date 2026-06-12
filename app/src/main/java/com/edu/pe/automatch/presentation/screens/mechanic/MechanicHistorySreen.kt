package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.edu.pe.automatch.presentation.navigation.Screen

data class HistoryService(
    val customer: String,
    val service: String,
    val date: String,
    val status: String,
    val amount: String
)

@Composable
fun MechanicHistoryScreen(
    navController: NavController
) {

    var selectedFilter by remember {
        mutableStateOf("30 Days")
    }

    val historyServices = listOf(
        HistoryService(
            customer = "Carolina Herrera",
            service = "Brake inspection",
            date = "March 12, 2025",
            status = "Completed",
            amount = "$100"
        ),
        HistoryService(
            customer = "Juanes Workshop",
            service = "Engine Check up",
            date = "June 12, 2025",
            status = "Completed",
            amount = "$250"
        ),
        HistoryService(
            customer = "Michael Torres",
            service = "Oil Change",
            date = "July 01, 2025",
            status = "Completed",
            amount = "$80"
        )
    )

    Scaffold(

        containerColor = Color(0xFFF3F1FC),

        bottomBar = {

            NavigationBar(
                containerColor = Color(0xFFEDE7F6)
            ) {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.MechanicDashboard.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text("Dashboard")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text("Requests")
                    }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text("Services")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.MechanicProfile.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text("Profile")
                    }
                )
            }
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
                                value = "12",
                                label = "Services"
                            )

                            HistoryStat(
                                value = "$800",
                                label = "This Month"
                            )

                            HistoryStat(
                                value = "4.9",
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
                                    if (isSelected)
                                        Color(0xFF5B50D6)
                                    else
                                        Color.Transparent
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
                                color = if (isSelected)
                                    Color.White
                                else
                                    Color(0xFF21195B),
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

            items(historyServices) { service ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                                    text = service.customer,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF14123B)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = service.service,
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = service.date,
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
                                        text = service.status,
                                        color = Color(0xFF27AE60),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = service.amount,
                                    fontSize = 26.sp,
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
            color = if (highlighted)
                Color(0xFFFFD479)
            else
                Color.White
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

    MechanicHistoryScreen(
        navController = rememberNavController()
    )
}