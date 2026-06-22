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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.ServiceItem
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }
    var services by remember { mutableStateOf<List<ServiceItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val serviceCatalogRepo = remember { RepositoryModule.provideServiceCatalogRepository() }

    LaunchedEffect(searchQuery) {
        isLoading = true
        val query = searchQuery.trim()
        val raw = if (query.isBlank()) {
            serviceCatalogRepo.getAllServices()
        } else {
            delay(350)
            serviceCatalogRepo.searchServices(query)
        }
        services = raw.filter { it.status?.uppercase() != "INACTIVE" }
        isLoading = false
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 1,
                navType = BottomNavType.DRIVER,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.DriverHome.route) {
                            launchSingleTop = true
                        }
                        1 -> { /* Search */ }
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
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Search",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(text = "eje. llantas, engine...", color = Color.Gray, fontSize = 14.sp)
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(22.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Primary)
                    }
                }
                services.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (searchQuery.isBlank()) "No services available"
                            else "No results for \"$searchQuery\"",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp)
                    ) {
                        items(services, key = { it.id }) { service ->
                            ServiceCard(
                                service = service,
                                onRequest = {
                                    navController.navigate(Screen.RequestServiceScreen.route)
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(12.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceItem,
    onRequest: () -> Unit
) {
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
                    text = service.title.ifBlank { "Service #${service.id}" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DarkGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (service.categoryName != null) {
                    Text(text = service.categoryName, fontSize = 13.sp, color = Primary, fontWeight = FontWeight.Medium)
                }
                if (service.description.isNotBlank()) {
                    Text(text = service.description, fontSize = 13.sp, color = Color.Gray, maxLines = 2)
                }
                val priceLabel = priceRangeLabel(service.minimumPrice, service.maximumPrice)
                if (priceLabel != null) {
                    Text(text = priceLabel, fontSize = 13.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            Button(
                onClick = onRequest,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(text = "Request", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

private fun priceRangeLabel(min: Double?, max: Double?): String? {
    return when {
        min != null && max != null -> "S/ ${min.toInt()} - S/ ${max.toInt()}"
        min != null -> "Desde S/ ${min.toInt()}"
        max != null -> "Hasta S/ ${max.toInt()}"
        else -> null
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    AutoMatchTheme {
        SearchScreen(rememberNavController())
    }
}
