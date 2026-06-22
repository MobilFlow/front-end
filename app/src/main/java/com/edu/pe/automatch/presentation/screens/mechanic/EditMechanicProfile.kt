package com.edu.pe.automatch.presentation.screens.mechanic

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.model.Specialty
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.components.MapComponent
import com.edu.pe.automatch.presentation.components.ProfileHeader
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditMechanicProfile(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userRepository = remember { RepositoryModule.provideUserRepository() }
    val mechanicRepository = remember { RepositoryModule.provideMechanicRepository() }

    var fullName by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var workshopName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var mechanic by remember { mutableStateOf<MechanicProfile?>(null) }
    var allSpecialties by remember { mutableStateOf<List<Specialty>>(emptyList()) }
    var selectedSpecialtyIds by remember { mutableStateOf<Set<Long>>(emptySet()) }
    var newSpecialtyName by remember { mutableStateOf("") }

    var resolvedLat by remember { mutableStateOf<Double?>(null) }
    var resolvedLng by remember { mutableStateOf<Double?>(null) }

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var isGeocoding by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var messageIsError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                fullName = user.fullName
                photoUrl = user.profilePicture ?: ""
                val mp = mechanicRepository.getMechanicByUserId(user.id)
                if (mp != null) {
                    mechanic = mp
                    description = mp.description ?: ""
                    address = mp.workshopAddress ?: ""
                    workshopName = mp.workshopName ?: ""
                    selectedSpecialtyIds = mp.specialties.map { it.id }.toSet()

                    val loc = mechanicRepository.getMechanicLocation(mp.id)
                    if (loc != null) {
                        resolvedLat = loc.latitude
                        resolvedLng = loc.longitude
                        if (address.isBlank()) address = loc.addressText ?: ""
                    }
                }
                allSpecialties = mechanicRepository.getAllSpecialties()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    fun geocodeAddress() {
        val query = address.trim()
        if (query.isBlank()) return
        scope.launch {
            isGeocoding = true
            message = null
            try {
                val coords = withContext(Dispatchers.IO) {
                    @Suppress("DEPRECATION")
                    val results = Geocoder(context, Locale.getDefault())
                        .getFromLocationName("$query, Peru", 1)
                    results?.firstOrNull()?.let { it.latitude to it.longitude }
                }
                if (coords != null) {
                    resolvedLat = coords.first
                    resolvedLng = coords.second
                    messageIsError = false
                    message = "Ubicación encontrada: ${"%.5f".format(coords.first)}, ${"%.5f".format(coords.second)}"
                } else {
                    messageIsError = true
                    message = "No se encontró esa dirección. Sé más específico (calle, distrito, ciudad)."
                }
            } catch (e: Exception) {
                messageIsError = true
                message = "Error al buscar la dirección: ${e.message}"
            } finally {
                isGeocoding = false
            }
        }
    }

    fun toggleSpecialty(spec: Specialty) {
        val mp = mechanic ?: return
        scope.launch {
            val updated = if (spec.id in selectedSpecialtyIds) {
                mechanicRepository.removeSpecialtyFromMechanic(mp.id, spec.id)
            } else {
                mechanicRepository.addSpecialtyToMechanic(mp.id, spec.id)
            }
            if (updated != null) {
                mechanic = updated
                selectedSpecialtyIds = updated.specialties.map { it.id }.toSet()
            } else {
                messageIsError = true
                message = "No se pudo actualizar la especialidad."
            }
        }
    }

    fun createAndAddSpecialty() {
        val name = newSpecialtyName.trim()
        val mp = mechanic ?: return
        if (name.isBlank()) return
        scope.launch {
            val created = mechanicRepository.createSpecialty(name)
            if (created != null) {
                allSpecialties = mechanicRepository.getAllSpecialties()
                val updated = mechanicRepository.addSpecialtyToMechanic(mp.id, created.id)
                if (updated != null) {
                    mechanic = updated
                    selectedSpecialtyIds = updated.specialties.map { it.id }.toSet()
                }
                newSpecialtyName = ""
            } else {
                messageIsError = true
                message = "No se pudo crear la especialidad."
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Column(modifier = Modifier.padding(20.dp)) {
                        ProfileHeader(
                            name = fullName.ifBlank { "Mechanic" },
                            imageUrl = photoUrl.ifBlank { null },
                            location = address.ifEmpty { "Peru" },
                            onEditClick = { navController.popBackStack() }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = "Name", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Full Name") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Profile Photo", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = photoUrl,
                            onValueChange = { photoUrl = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Image URL") },
                            placeholder = { Text("https://...") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Workshop Name", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = workshopName,
                            onValueChange = { workshopName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Workshop Name") }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "Presentation", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            placeholder = { Text("Write about your experience...") }
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "Specialties", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (allSpecialties.isEmpty()) {
                            Text("No specialties available yet. Create one below.", fontSize = 13.sp, color = Color.Gray)
                        }
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            allSpecialties.forEach { spec ->
                                FilterChip(
                                    selected = spec.id in selectedSpecialtyIds,
                                    onClick = { toggleSpecialty(spec) },
                                    label = { Text(spec.name) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = newSpecialtyName,
                                onValueChange = { newSpecialtyName = it },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                label = { Text("New specialty") },
                                placeholder = { Text("e.g. transmission") }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { createAndAddSpecialty() },
                                enabled = newSpecialtyName.isNotBlank()
                            ) {
                                Text("Add")
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "Location", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            label = { Text("Workshop Address") },
                            placeholder = { Text("Av. Javier Prado 123, San Isidro, Lima") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { geocodeAddress() },
                            enabled = address.isNotBlank() && !isGeocoding,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isGeocoding) "Searching..." else "Find on map")
                        }

                        if (resolvedLat != null && resolvedLng != null) {
                            val lat = resolvedLat!!
                            val lng = resolvedLng!!
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Lat: ${"%.5f".format(lat)}  ·  Lng: ${"%.5f".format(lng)}",
                                fontSize = 13.sp, color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            MapComponent(
                                latitude = lat,
                                longitude = lng,
                                title = workshopName.ifBlank { fullName }
                            )
                        }

                        if (message != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(message!!, color = if (messageIsError) Color.Red else Color(0xFF2E7D32))
                        }

                        Spacer(modifier = Modifier.height(28.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    isSaving = true
                                    message = null
                                    try {
                                        userRepository.updateProfile(fullName = fullName, profilePicture = photoUrl)

                                        val mp = mechanic
                                        var allOk = true
                                        if (mp != null) {
                                            val updatedMechanic = mp.copy(
                                                description = description,
                                                workshopName = workshopName.ifBlank { mp.workshopName },
                                                workshopAddress = address
                                            )
                                            val ok = mechanicRepository.updateMechanic(updatedMechanic)
                                            if (ok) mechanic = updatedMechanic else allOk = false

                                            val lat = resolvedLat
                                            val lng = resolvedLng
                                            if (lat != null && lng != null) {
                                                val loc = mechanicRepository.upsertMechanicLocation(
                                                    mechanicId = mp.id,
                                                    latitude = lat,
                                                    longitude = lng,
                                                    addressText = address
                                                )
                                                if (loc == null) allOk = false
                                            }
                                        }

                                        isSaving = false
                                        if (allOk) {
                                            navController.popBackStack()
                                        } else {
                                            messageIsError = true
                                            message = "Algunos cambios no se guardaron en el servidor. Revisa el log MECHANIC_DEBUG."
                                        }
                                    } catch (e: Exception) {
                                        isSaving = false
                                        messageIsError = true
                                        message = "Error: ${e.message}"
                                    }
                                }
                            },
                            enabled = !isSaving,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isSaving) "Saving..." else "Save Changes")
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
                when (index) {
                    0 -> navController.navigate(Screen.MechanicDashboard.route)
                    1 -> navController.navigate(Screen.MechanicRequests.route)
                    2 -> navController.navigate(Screen.MechanicServices.route)
                    3 -> {}
                }
            }
        )
    }
}
