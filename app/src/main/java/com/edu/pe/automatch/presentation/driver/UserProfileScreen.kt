package com.edu.pe.automatch.presentation.driver

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.components.ProfileAvatar
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AccentBlue
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.PrimaryDark
import com.edu.pe.automatch.presentation.theme.PrimaryLight
import com.edu.pe.automatch.presentation.theme.SoftBackground
import android.util.Log
import kotlinx.coroutines.launch

private const val TAG = "UserProfileScreen"

@Composable
fun UserProfileScreen(
    navController: NavController
) {
    var userName by remember { mutableStateOf("") }
    var userInitials by remember { mutableStateOf("") }
    var profilePicture by remember { mutableStateOf<String?>(null) }
    var cars by remember { mutableStateOf<List<Car>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var showAddVehicleDialog by remember { mutableStateOf(false) }
    var showEditPhotoDialog by remember { mutableStateOf(false) }
    var carToDelete by remember { mutableStateOf<Car?>(null) }
    var refreshKey by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val driverRepo = remember { RepositoryModule.provideDriverRepository() }

    LaunchedEffect(refreshKey) {
        try {
            val user = userRepo.getCurrentUser()
            Log.d(TAG, "user=$user")
            if (user != null) {
                userName = user.fullName
                userInitials = user.fullName.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }
                profilePicture = user.profilePicture
                val dp = driverRepo.getDriverByUserId(user.id)
                Log.d(TAG, "driverProfile=$dp")
                if (dp != null) {
                    cars = driverRepo.getCarsByDriverProfile(dp.id)
                    Log.d(TAG, "cars=${cars.size}")
                } else {
                    Log.d(TAG, "driverProfile is NULL, cars not fetched")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading data", e)
        }
        loading = false
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 3,
                navType = BottomNavType.DRIVER,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.DriverHome.route) { launchSingleTop = true }
                        1 -> navController.navigate(Screen.DriverSearch.route)
                        2 -> navController.navigate(Screen.DriverHistory.route)
                        3 -> { }
                    }
                }
            )
        }
    ) { innerPadding ->

        if (loading) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(SoftBackground)
                    .verticalScroll(scrollState)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(140.dp).background(PrimaryDark)
                    )
                    ProfileAvatar(
                        imageUrl = profilePicture,
                        initials = userInitials,
                        size = 90.dp,
                        backgroundColor = Primary,
                        contentColor = Color.White,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 45.dp)
                    )
                }

                Spacer(modifier = Modifier.height(56.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { showEditPhotoDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userName.ifEmpty { "User" },
                    fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkGray,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                )

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
                        ProfileStat(label = "Vehicles", value = cars.size.toString())
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "My Vehicles",
                    fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkGray,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                cars.forEach { car ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("${car.brand} ${car.model}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = DarkGray)
                                Text("Year: ${car.year}", fontSize = 13.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SuggestionChip(
                                    onClick = {},
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = AccentBlue.copy(alpha = 0.1f),
                                        labelColor = Primary
                                    ),
                                    border = null,
                                    label = { Text(car.plate, fontSize = 12.sp) }
                                )
                                IconButton(onClick = { carToDelete = car }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar vehículo",
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (cars.isEmpty()) {
                    Text(
                        "No vehicles registered",
                        color = Color.Gray, fontSize = 14.sp,
                        modifier = Modifier.padding(start = 20.dp, top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showAddVehicleDialog = true },
                    modifier = Modifier.padding(horizontal = 20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Vehicle")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAddVehicleDialog) {
        AddVehicleDialog(
            onDismiss = { showAddVehicleDialog = false },
            onAdd = {
                showAddVehicleDialog = false
                refreshKey++
            }
        )
    }

    if (showEditPhotoDialog) {
        EditProfileDialog(
            currentName = userName,
            currentUrl = profilePicture,
            onDismiss = { showEditPhotoDialog = false },
            onSave = { name, url ->
                scope.launch {
                    val updated = userRepo.updateProfile(name, url)
                    if (updated != null) {
                        userName = updated.fullName
                        userInitials = updated.fullName.split(" ").filter { it.isNotBlank() }
                            .take(2).joinToString("") { it.first().uppercase() }
                        profilePicture = updated.profilePicture
                    }
                    showEditPhotoDialog = false
                }
            }
        )
    }

    carToDelete?.let { car ->
        DeleteCarDialog(
            car = car,
            onDismiss = { carToDelete = null },
            onConfirm = {
                scope.launch {
                    val ok = driverRepo.deleteCar(car.id)
                    carToDelete = null
                    if (ok) refreshKey++
                }
            }
        )
    }
}

@Composable
private fun EditProfileDialog(
    currentName: String,
    currentUrl: String?,
    onDismiss: () -> Unit,
    onSave: (String?, String?) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var url by remember { mutableStateOf(currentUrl.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar perfil", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Tu nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL de la foto") },
                    placeholder = { Text("https://...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Pega la URL de una imagen para la foto de perfil.",
                    fontSize = 12.sp, color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(name.trim().ifBlank { null }, url.trim().ifBlank { null }) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun DeleteCarDialog(
    car: Car,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar vehículo", fontWeight = FontWeight.Bold) },
        text = {
            Text("¿Seguro que quieres eliminar ${car.brand} ${car.model} (${car.plate})? Esta acción no se puede deshacer.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F), contentColor = Color.White)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun AddVehicleDialog(
    onDismiss: () -> Unit,
    onAdd: () -> Unit
) {
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var fuelType by remember { mutableStateOf("") }
    var fuelTypeExpanded by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val driverRepo = remember { RepositoryModule.provideDriverRepository() }

    val fuelTypes = listOf("GASOLINE", "DIESEL", "HYBRID", "ELECTRIC", "GAS")
    val fuelTypeLabels = mapOf(
        "GASOLINE" to "Gasolina",
        "DIESEL" to "Diésel",
        "HYBRID" to "Híbrido",
        "ELECTRIC" to "Eléctrico",
        "GAS" to "Gas"
    )

    val isFormValid = brand.isNotBlank() && model.isNotBlank() && year.isNotBlank() && plate.isNotBlank()

    AlertDialog(
        onDismissRequest = { if (!isSubmitting) onDismiss() },
        title = { Text("Agregar Vehículo", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marca") },
                    placeholder = { Text("Ej: Toyota") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Modelo") },
                    placeholder = { Text("Ej: Corolla") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it.filter { c -> c.isDigit() }.take(4) },
                    label = { Text("Año") },
                    placeholder = { Text("Ej: 2020") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    label = { Text("Placa") },
                    placeholder = { Text("Ej: ABC-123") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedTextField(
                        value = fuelTypeLabels[fuelType] ?: "",
                        onValueChange = {},
                        label = { Text("Tipo de Combustible") },
                        placeholder = { Text("Seleccionar") },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
                    )
                    DropdownMenu(
                        expanded = fuelTypeExpanded,
                        onDismissRequest = { fuelTypeExpanded = false }
                    ) {
                        fuelTypes.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(fuelTypeLabels[option] ?: option) },
                                onClick = {
                                    fuelType = option
                                    fuelTypeExpanded = false
                                }
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.matchParentSize().clickable { fuelTypeExpanded = true }
                    )
                }
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage!!, color = Color.Red, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!isFormValid) return@Button
                    scope.launch {
                        isSubmitting = true
                        errorMessage = null
                        try {
                            val user = userRepo.getCurrentUser()
                            Log.d(TAG, "dialog user=$user")
                            if (user != null) {
                                val dp = driverRepo.getDriverByUserId(user.id)
                                Log.d(TAG, "dialog driverProfile=$dp")
                                if (dp != null) {
                                    driverRepo.createCar(
                                        driverProfileId = dp.id,
                                        brand = brand.trim(),
                                        model = model.trim(),
                                        year = year.toIntOrNull() ?: 2024,
                                        plate = plate.trim(),
                                        fuelType = fuelType.ifBlank { null }
                                    )
                                } else {
                                    errorMessage = "No se encontró perfil de conductor. ¿Creaste cuenta como Driver?"
                                    isSubmitting = false
                                    return@launch
                                }
                            } else {
                                errorMessage = "Usuario no encontrado. Inicia sesión de nuevo."
                                isSubmitting = false
                                return@launch
                            }
                            isSubmitting = false
                            onAdd()
                        } catch (e: Exception) {
                            isSubmitting = false
                            errorMessage = "Error: ${e.message}"
                        }
                    }
                },
                enabled = isFormValid && !isSubmitting
            ) {
                Text(if (isSubmitting) "Guardando..." else "Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = { if (!isSubmitting) onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun ProfileStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Primary)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    AutoMatchTheme {
        UserProfileScreen(rememberNavController())
    }
}
