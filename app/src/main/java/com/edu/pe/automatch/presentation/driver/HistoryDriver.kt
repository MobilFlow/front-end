package com.edu.pe.automatch.presentation.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.SoftBackground
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private data class ServiceHistory(
    val id: String,
    val title: String,
    val mechanic: String,
    val date: LocalDate,
    val price: Int
)

private val allServices = listOf(
    ServiceHistory("1", "Brake inspection",   "Jorge Ramírez",   LocalDate.of(2025, 3, 12), 100),
    ServiceHistory("2", "Engine Check up",    "Juanes Workshop", LocalDate.of(2025, 6, 12), 250),
    ServiceHistory("3", "General Inspection", "Melisa Torres",   LocalDate.of(2025, 5,  2),  87),
    ServiceHistory("4", "Oil Change",         "Carlos M.",       LocalDate.of(2025, 6,  1),  45),
    ServiceHistory("5", "Tire Rotation",      "Ana L.",          LocalDate.of(2025, 5, 28),  60)
)

private val dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")

private enum class HistoryFilter(val label: String) {
    ALL("All"),
    DAYS_20("20 days"),
    DAYS_30("30 days")
}

@Composable
fun HistoryScreen(
    navController: NavController
) {
    var activeFilter by remember { mutableStateOf(HistoryFilter.ALL) }
    val today = LocalDate.now()

    val filteredServices = remember(activeFilter) {
        when (activeFilter) {
            HistoryFilter.ALL     -> allServices
            HistoryFilter.DAYS_20 -> allServices.filter { ChronoUnit.DAYS.between(it.date, today) <= 20 }
            HistoryFilter.DAYS_30 -> allServices.filter { ChronoUnit.DAYS.between(it.date, today) <= 30 }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 2,
                navType = BottomNavType.DRIVER,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.DriverHome.route) {
                            launchSingleTop = true
                        }
                        1 -> navController.navigate(Screen.DriverSearch.route)
                        2 -> { /* History - ya estamos aquí */ }
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
                text = "History",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            FilterTabRow(
                activeFilter = activeFilter,
                onFilterSelected = { activeFilter = it },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                items(filteredServices, key = { it.id }) { service ->
                    ServiceHistoryCard(service = service)
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun FilterTabRow(
    activeFilter: HistoryFilter,
    onFilterSelected: (HistoryFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HistoryFilter.entries.forEach { filter ->
                val isActive = filter == activeFilter
                Card(
                    onClick = { onFilterSelected(filter) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) Primary else Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = filter.label,
                        fontSize = 14.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        color = if (isActive) Color.White else Color.Gray,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceHistoryCard(service: ServiceHistory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = service.mechanic, fontSize = 13.sp, color = Color.Gray)
                Text(text = service.date.format(dateFormatter), fontSize = 13.sp, color = Color.Gray)
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SoftBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "$${service.price}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkGray,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    AutoMatchTheme {
        HistoryScreen(rememberNavController())
    }
}