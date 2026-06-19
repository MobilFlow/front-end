package com.edu.pe.automatch.presentation.driver

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.edu.pe.automatch.data.remote.dtos.ReviewResponseDto
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.presentation.components.MapComponent
import com.edu.pe.automatch.presentation.components.SpecialtyChip
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AccentBlue
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.PrimaryDark
import com.edu.pe.automatch.presentation.theme.SoftBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicProfileScreen(
    navController: NavController,
    mechanicId: String = "",
    viewModel: MechanicProfileViewModel = viewModel(
        factory = remember {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MechanicProfileViewModel(
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

    LaunchedEffect(mechanicId) {
        viewModel.loadMechanicProfile(mechanicId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mechanic's profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = DarkGray,
                    navigationIconContentColor = DarkGray
                )
            )
        },
        bottomBar = {
            if (uiState is MechanicProfileUiState.Success) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoftBackground)
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(Screen.RequestServiceScreen.route) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Request service", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    ) { padding ->
        when (val state = uiState) {
            is MechanicProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            }
            is MechanicProfileUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = Color.Red, textAlign = TextAlign.Center)
                }
            }
            is MechanicProfileUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text(text = "Mechanic not found", color = DarkGray)
                }
            }
            is MechanicProfileUiState.Success -> {
                val mechanic = state.mechanic
                val fullName = state.fullName
                val initials = fullName.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }
                val reputation = state.reputation
                val location = state.location

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(SoftBackground)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(PrimaryDark)
                        )
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Primary)
                                .align(Alignment.BottomCenter)
                                .offset(y = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(52.dp))

                    Text(
                        text = fullName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AccentBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Certified Mechanic", color = AccentBlue, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ProfileStat(label = "Rating", value = String.format("%.1f", reputation?.averageRating ?: 0.0))
                            ProfileStat(label = "Reviews", value = (reputation?.totalReviews ?: 0).toString())
                            ProfileStat(label = "Workshop", value = mechanic.workshopName ?: "N/A")
                        }
                    }

                    if (!mechanic.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Presentation", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray, modifier = Modifier.padding(horizontal = 20.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Text(
                                text = mechanic.description,
                                fontSize = 14.sp, color = Color.Gray, lineHeight = 22.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    if (mechanic.specialties.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Specialties", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray, modifier = Modifier.padding(horizontal = 20.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            mechanic.specialties.forEach { spec ->
                                SpecialtyChip(label = spec.name)
                            }
                        }
                    }

                    if (!mechanic.workshopAddress.isNullOrBlank() || location != null) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Location", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray, modifier = Modifier.padding(horizontal = 20.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = location?.addressText ?: mechanic.workshopAddress ?: "N/A",
                                        fontSize = 14.sp, color = DarkGray
                                    )
                                }
                                
                                if (location != null) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    MapComponent(
                                        latitude = location.latitude,
                                        longitude = location.longitude,
                                        title = mechanic.workshopName ?: "Workshop"
                                    )
                                }
                            }
                        }
                    }

                    // Reviews Section
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Reviews", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                        TextButton(onClick = { /* Read more functionality if needed */ }) {
                            Text("Read More", color = Primary)
                        }
                    }
                    
                    if (state.reviews.isEmpty()) {
                        Text(
                            "No reviews yet",
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    } else {
                        state.reviews.forEach { review ->
                            ReviewItem(review)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Primary)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun ReviewItem(review: ReviewResponseDto) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB400),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = review.createdAt?.take(10) ?: "",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.content,
                fontSize = 14.sp,
                color = DarkGray,
                lineHeight = 20.sp
            )
            if (review.edited) {
                Text(
                    text = "(Edited)",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
