package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.components.ProfileHeader
import com.edu.pe.automatch.presentation.components.SpecialtyChip
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.SoftBackground

@Composable
fun EditMechanicProfile(navController: NavController) {
    var fullName by remember { mutableStateOf("Loading...") }
    var locationText by remember { mutableStateOf("Certified Mechanic") }
    val description = remember { mutableStateOf("") }
    val workshopAddress = remember { mutableStateOf("") }
    
    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val mechanicRepo = remember { RepositoryModule.provideMechanicRepository() }

    LaunchedEffect(Unit) {
        userRepo.getCurrentUser()?.let { user ->
            fullName = user.fullName
            mechanicRepo.getMechanicByUserId(user.id)?.let { mechanic ->
                description.value = mechanic.description ?: ""
                workshopAddress.value = mechanic.workshopAddress ?: ""
                locationText = "Certified Mechanic - ${mechanic.workshopAddress ?: "Peru"}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileHeader(
                        name = fullName,
                        location = locationText,
                        onEditClick = { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Presentation", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { description.value = it },
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        placeholder = { Text("Write about your experience...") }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Specialties", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Estas podrían venir del backend en el futuro
                        SpecialtyChip(label = "Brakes")
                        SpecialtyChip(label = "Engine")
                        SpecialtyChip(label = "Suspension")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Location", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = workshopAddress.value,
                        onValueChange = { workshopAddress.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Workshop Address") }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { /* TODO: Implement Update API call */ navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Changes")
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        BottomNavBar(
            selectedItem = 3,
            navType = BottomNavType.MECHANIC,
            onItemSelected = { index ->
                when(index) {
                    0 -> navController.navigate(Screen.MechanicDashboard.route)
                    3 -> navController.navigate(Screen.MechanicProfile.route)
                }
            }
        )
    }
}
