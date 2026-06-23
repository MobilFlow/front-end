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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
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
    val lifecycleOwner = LocalLifecycleOwner.current

    // Sincronización de datos al volver a la pantalla
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.loadCurrentMechanicProfile()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
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
                    val profilePicture = state.profilePicture
                    val reputation = state.reputation
                    val reviews = state.reviews
                    val location = state.location

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Spacer(modifier = Modifier.height(12.dp))
                                ProfileHeader(
                                    name = fullName,
                                    imageUrl = profilePicture,
                                    location = "Certified Mechanic · ${location?.addressText ?: mechanic?.workshopAddress ?: "Profile incomplete"}",
                                    onEditClick = {
                                        navController.navigate(Screen.EditMechanicProfile.route)
                                    }
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        StatCard(
                                            value = String.format(Locale.getDefault(), "%.1f", reputation?.averageRating ?: 0.0),
                                            label = "Rating",
                                            modifier = Modifier.width(140.dp)
                                        )
                                        StatCard(
                                            value = (reputation?.totalReviews ?: 0).toString(),
                                            label = "Reviews",
                                            modifier = Modifier.width(140.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(24.dp))

                                Text(text = "Presentation", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = mechanic?.description ?: "Welcome! Please edit your profile to add a description of your services.",
                                    fontSize = 14.sp,
                                    color = if (mechanic?.description == null) Color.Gray else Color.Unspecified
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
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Primary)
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(location?.addressText ?: mechanic?.workshopAddress ?: "Address not set")
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                // Lógica de Mapa Dinámico
                                if (location != null) {
                                    MapComponent(
                                        latitude = location.latitude,
                                        longitude = location.longitude,
                                        title = mechanic?.workshopName ?: fullName
                                    )
                                } else {
                                    MapComponent(address = mechanic?.workshopAddress)
                                }

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
                    1 -> navController.navigate(Screen.MechanicRequests.route) { launchSingleTop = true }
                    2 -> navController.navigate(Screen.MechanicServices.route)
                    3 -> {} // Profile (Actual)
                }
            }
        )
    }
}
