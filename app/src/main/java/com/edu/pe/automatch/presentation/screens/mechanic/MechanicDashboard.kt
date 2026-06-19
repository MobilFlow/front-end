package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.SoftBackground

@Composable
fun MechanicDashboard(navController: NavController) {
    var userName by remember { mutableStateOf("Loading...") }
    val userRepo = remember { RepositoryModule.provideUserRepository() }

    LaunchedEffect(Unit) {
        userRepo.getCurrentUser()?.let {
            userName = it.fullName
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 0,
                navType = BottomNavType.MECHANIC,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.MechanicDashboard.route) { launchSingleTop = true }
                        1 -> navController.navigate(Screen.MechanicRequests.route)
                        2 -> navController.navigate(Screen.MechanicHistory.route)
                        3 -> navController.navigate(Screen.MechanicProfile.route)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SoftBackground)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Welcome back,", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        text = userName,
                        color = DarkGray,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { /* Notifications */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Primary)
                }
            }

            // Cards de resumen hardcodeados por ahora, pero el nombre ya es dinámico
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Today's Summary", fontWeight = FontWeight.Bold, color = DarkGray)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("You have 0 pending requests.", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}
