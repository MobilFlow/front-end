// MechanicProfile.kt
package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.edu.pe.automatch.R
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.MapComponent
import com.edu.pe.automatch.presentation.components.ProfileHeader
import com.edu.pe.automatch.presentation.components.ReviewCard
import com.edu.pe.automatch.presentation.components.SpecialtyChip
import com.edu.pe.automatch.presentation.components.StatCard
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.SoftBackground
import androidx.navigation.NavController
import com.edu.pe.automatch.presentation.navigation.Screen
import androidx.navigation.compose.rememberNavController

@Composable
fun MechanicProfile(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            item {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    ProfileHeader(
                        name = "Jorge Ramirez",
                        location = "Certified Mechanic - Callao, Peru",

                        onEditClick = {
                            navController.navigate(
                                Screen.EditMechanicProfile.route
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        StatCard(
                            value = "8",
                            label = "Years exp."
                        )

                        StatCard(
                            value = "400",
                            label = "Services"
                        )

                        StatCard(
                            value = "98%",
                            label = "Punctuality"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Presentation",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Certified mechanic with 8 years of experience in brakes, engines, and electrical systems. On-site service at my workshop.",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Specialties",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        SpecialtyChip(label = "Brakes")
                        SpecialtyChip(label = "Engine")
                        SpecialtyChip(label = "Suspension")
                        SpecialtyChip(label = "SUV")
                        SpecialtyChip(label = "Sedan")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row {

                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text("La Perla Callao 333")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {

                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text("Monday to Friday · 7:00am - 8:00pm")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    MapComponent()

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Reviews",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ReviewCard(
                        reviewerName = "Ana L.",
                        review = "He was professional and explained everything clearly."
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        BottomNavBar(
            selectedItem = 3,

            onItemSelected = { index ->

                when(index) {

                    0 -> {
                        navController.navigate(
                            Screen.MechanicDashboard.route
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MechanicProfilePreview() {

    val navController = rememberNavController()

    AutoMatchTheme {

        MechanicProfile(navController)
    }
}