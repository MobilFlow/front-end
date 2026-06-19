package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.components.MapComponent
import com.edu.pe.automatch.presentation.components.ProfileHeader
import com.edu.pe.automatch.presentation.components.ReviewCard
import com.edu.pe.automatch.presentation.components.SpecialtyChip
import com.edu.pe.automatch.presentation.components.StatCard
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.SoftBackground
import java.util.Locale

@Composable
fun MechanicProfile(
    navController: NavController,
    viewModel: OwnMechanicProfileViewModel = viewModel(
        factory = remember {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return OwnMechanicProfileViewModel(
                        RepositoryModule.provideUserRepository(),
                        RepositoryModule.provideMechanicRepository(),
                        RepositoryModule.provideReviewRepository()
                    ) as T
                }
            }
        }
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentMechanicProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {
                is OwnMechanicProfileUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Primary)
                    }
                }
                is OwnMechanicProfileUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Error", fontWeight = FontWeight.Bold, color = Color.Red)
                            Text(text = state.message, textAlign = TextAlign.Center, modifier = Modifier.padding(20.dp))
                        }
                    }
                }
                is OwnMechanicProfileUiState.Success -> {
                    val mechanic = state.mechanic
                    val fullName = state.fullName
                    val reputation = state.reputation
                    val reviews = state.reviews

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Column(modifier = Modifier.padding(20.dp)) {
                                ProfileHeader(
                                    name = fullName,
                                    location = "Certified Mechanic · ${mechanic?.workshopAddress ?: "Profile incomplete"}",
                                    onEditClick = {
                                        navController.navigate(Screen.EditMechanicProfile.route)
                                    }
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    StatCard(
                                        value = String.format(Locale.getDefault(), "%.1f", reputation?.averageRating ?: 0.0),
                                        label = "Rating"
                                    )
                                    StatCard(
                                        value = (reputation?.totalReviews ?: 0).toString(),
                                        label = "Reviews"
                                    )
                                    StatCard(
                                        value = "New", 
                                        label = "Status"
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(24.dp))

                                Text(text = "Presentation", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = mechanic?.description ?: "Welcome! Please edit your profile to add a description of your services.",
                                    fontSize = 14.sp,
                                    color = if (mechanic == null) Color.Gray else Color.Unspecified
                                )

                                Spacer(modifier = Modifier.height(24.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(24.dp))

                                if (mechanic != null && mechanic.specialties.isNotEmpty()) {
                                    Text(text = "Specialties", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    @OptIn(ExperimentalLayoutApi::class)
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        mechanic.specialties.forEach { specialty ->
                                            SpecialtyChip(label = specialty.name)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(24.dp))
                                    HorizontalDivider()
                                    Spacer(modifier = Modifier.height(24.dp))
                                }

                                Text(text = "Location", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(12.dp))
                                Row {
                                    Icon(Icons.Default.LocationOn, contentDescription = null)
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(mechanic?.workshopAddress ?: "Address not set")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row {
                                    Icon(Icons.Default.Schedule, contentDescription = null)
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text("Mon - Fri · 7:00am - 8:00pm")
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                MapComponent()
                                Spacer(modifier = Modifier.height(24.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(24.dp))

                                Text(text = "Reviews", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                if (reviews.isEmpty()) {
                                    Text("No reviews yet.", fontSize = 14.sp, color = Color.Gray)
                                }
                            }
                        }
                        
                        items(reviews) { review ->
                            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                                ReviewCard(
                                    reviewerName = "Automatch Client",
                                    review = review.content
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }

        BottomNavBar(
            selectedItem = 3,
            navType = BottomNavType.MECHANIC,
            onItemSelected = { index ->
                when(index) {
                    0 -> navController.navigate(Screen.MechanicDashboard.route) { launchSingleTop = true }
                    1 -> navController.navigate(Screen.MechanicRequests.route)
                    2 -> navController.navigate(Screen.MechanicHistory.route)
                    3 -> { /* Actual */ }
                }
            }
        )
    }
}
