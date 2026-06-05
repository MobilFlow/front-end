// MechanicDashboard.kt
package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.StatCard
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.SoftBackground
import androidx.navigation.NavController
import com.edu.pe.automatch.presentation.navigation.Screen
import androidx.navigation.compose.rememberNavController

@Composable
fun MechanicDashboard(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(20.dp)
        ) {

            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Jorge Ramirez",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                StatCard(
                    value = "1",
                    label = "Requests"
                )

                StatCard(
                    value = "$800",
                    label = "This week"
                )

                StatCard(
                    value = "4.9",
                    label = "Rating"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "New request",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Carolina Herrera",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Toyota Tundra 2026")

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Brakes · 12km · 4.6 ★")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "In progress",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Oil Change",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Juan Perez · Mazda")

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("$50")
                }
            }
        }

        BottomNavBar(
            selectedItem = 0,
            onItemSelected = { index ->
                when(index) {
                    0 -> {
                        navController.navigate(Screen.MechanicDashboard.route) {
                            popUpTo(Screen.MechanicDashboard.route) { inclusive = true }
                        }
                    }
                    3 -> {
                        navController.navigate(Screen.MechanicProfile.createRoute("1"))
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MechanicDashboardPreview() {

    val navController = rememberNavController()

    AutoMatchTheme {

        MechanicDashboard(navController)
    }
}