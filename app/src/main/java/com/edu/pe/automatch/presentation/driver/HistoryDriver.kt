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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

private val dateFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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
    var historyItems by remember { mutableStateOf<List<ServiceRequestInfo>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val driverRepo = remember { RepositoryModule.provideDriverRepository() }
    val serviceRequestRepo = remember { RepositoryModule.provideServiceRequestRepository() }

    LaunchedEffect(Unit) {
        try {
            val user = userRepo.getCurrentUser()
            if (user != null) {
                val dp = driverRepo.getDriverByUserId(user.id)
                if (dp != null) {
                    historyItems = serviceRequestRepo.getServiceHistory(dp.id)
                }
            }
        } catch (_: Exception) {
        }
        loading = false
    }

    val filteredServices = remember(activeFilter, historyItems) {
        val today = Date()
        when (activeFilter) {
            HistoryFilter.ALL -> historyItems
            HistoryFilter.DAYS_20 -> historyItems.filter { item ->
                val date = parseDate(item.completedAt ?: item.createdAt ?: "")
                date != null && daysBetween(date, today) <= 20
            }
            HistoryFilter.DAYS_30 -> historyItems.filter { item ->
                val date = parseDate(item.completedAt ?: item.createdAt ?: "")
                date != null && daysBetween(date, today) <= 30
            }
        }
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

            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
                ) {
                    items(filteredServices, key = { it.id.toString() }) { service ->
                        ServiceHistoryCard(service = service)
                    }
                    if (filteredServices.isEmpty()) {
                        item {
                            Text(
                                "No service history",
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
            modifier = Modifier.fillMaxWidth().padding(4.dp),
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
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ServiceHistoryCard(service: ServiceRequestInfo) {
    val dateStr = service.completedAt ?: service.createdAt ?: ""
    val displayDate = try {
        val d = parseDate(dateStr)
        d?.let { dateFormatter.format(it) } ?: dateStr
    } catch (_: Exception) { dateStr }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.description ?: "Service #${service.id}",
                    fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Status: ${service.status ?: "Unknown"}", fontSize = 13.sp, color = Color.Gray)
                Text(text = displayDate, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

private fun parseDate(dateStr: String): Date? {
    return try {
        isoFormat.parse(dateStr.take(10))
    } catch (_: Exception) { null }
}

private fun daysBetween(d1: Date, d2: Date): Long {
    val diff = Math.abs(d2.time - d1.time)
    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    AutoMatchTheme {
        HistoryScreen(rememberNavController())
    }
}
