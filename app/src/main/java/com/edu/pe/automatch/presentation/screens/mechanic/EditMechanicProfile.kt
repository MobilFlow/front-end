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
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.components.ProfileHeader
import com.edu.pe.automatch.presentation.components.SpecialtyChip
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditMechanicProfile(navController: NavController) {
    val scope = rememberCoroutineScope()
    val userRepository = remember { RepositoryModule.provideUserRepository() }
    val mechanicRepository = remember { RepositoryModule.provideMechanicRepository() }

    var fullName by remember { mutableStateOf("Loading...") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var mechanic by remember { mutableStateOf<MechanicProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                fullName = user.fullName
                val mechanicProfile = mechanicRepository.getMechanicByUserId(user.id)
                if (mechanicProfile != null) {
                    mechanic = mechanicProfile
                    description = mechanicProfile.description ?: ""
                    address = mechanicProfile.workshopAddress ?: ""
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Column(modifier = Modifier.padding(20.dp)) {
                        ProfileHeader(
                            name = fullName,
                            location = address.ifEmpty { "Peru" },
                            onEditClick = { navController.popBackStack() }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "Presentation", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            placeholder = { Text("Write about your experience...") }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "Specialties", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            mechanic?.specialties?.forEach {
                                SpecialtyChip(label = it.name)
                            }
                            if (mechanic?.specialties.isNullOrEmpty()) {
                                SpecialtyChip(label = "Brakes")
                                SpecialtyChip(label = "Engine")
                                SpecialtyChip(label = "Suspension")
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "Location", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Workshop Address") }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                mechanic?.let { currentMechanic ->
                                    val updatedMechanic = currentMechanic.copy(
                                        description = description,
                                        workshopAddress = address
                                    )
                                    scope.launch {
                                        try {
                                            val success = mechanicRepository.updateMechanic(updatedMechanic)
                                            if (success) {
                                                navController.popBackStack()
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        ) {
                            Text("Save Changes")
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }

        BottomNavBar(
            selectedItem = 3,
            navType = BottomNavType.MECHANIC,
            onItemSelected = { index ->
                when(index) {
                    0 -> navController.navigate(Screen.MechanicDashboard.route)
                    1 -> navController.navigate(Screen.MechanicRequests.route)
                    2 -> navController.navigate(Screen.MechanicHistory.route)
                    3 -> {}
                }
            }
        )
    }
}
