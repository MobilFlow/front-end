package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.domain.model.ServiceCategory
import com.edu.pe.automatch.domain.model.ServiceItem
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.launch

private fun ServiceItem.isActive() = status?.uppercase() == "ACTIVE"

@Composable
fun MechanicServicesScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val mechanicRepo = remember { RepositoryModule.provideMechanicRepository() }
    val catalogRepo = remember { RepositoryModule.provideServiceCatalogRepository() }

    var mechanicProfileId by remember { mutableStateOf<Long?>(null) }
    var services by remember { mutableStateOf<List<ServiceItem>>(emptyList()) }
    var categories by remember { mutableStateOf<List<ServiceCategory>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var refreshKey by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf<String?>(null) }

    var editing by remember { mutableStateOf<ServiceItem?>(null) }

    LaunchedEffect(refreshKey) {
        loading = true
        try {
            val user = userRepo.getCurrentUser()
            val mp = if (user != null) mechanicRepo.getMechanicByUserId(user.id) else null
            mechanicProfileId = mp?.id
            if (mp != null) {
                services = catalogRepo.getServicesByMechanic(mp.id)
            }
            if (categories.isEmpty()) categories = catalogRepo.getCategories()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            loading = false
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = 2,
                navType = BottomNavType.MECHANIC,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Screen.MechanicDashboard.route) { launchSingleTop = true }
                        1 -> navController.navigate(Screen.MechanicRequests.route)
                        2 -> {}
                        3 -> navController.navigate(Screen.MechanicProfile.route)
                    }
                }
            )
        }
    ) { padding ->
        if (loading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(SoftBackground)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("My Services", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkGray)
                    Button(
                        onClick = { navController.navigate(Screen.CreateService.route) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                if (message != null) {
                    Text(message!!, color = Color(0xFFB00020), fontSize = 13.sp, modifier = Modifier.padding(horizontal = 20.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (mechanicProfileId == null) {
                    Text(
                        "No se encontró tu perfil de mecánico.",
                        color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(20.dp)
                    )
                } else if (services.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aún no has publicado servicios.", color = Color.Gray, fontSize = 14.sp)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
                    ) {
                        items(services, key = { it.id }) { service ->
                            ServiceManageCard(
                                service = service,
                                onEdit = { editing = service },
                                onToggle = {
                                    scope.launch {
                                        message = null
                                        val updated = catalogRepo.setServiceActive(service.id, !service.isActive())
                                        if (updated != null) refreshKey++
                                        else message = "No se pudo cambiar el estado (revisa SERVICE_DEBUG; puede faltar el endpoint activate)."
                                    }
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(12.dp)) }
                    }
                }
            }
        }
    }

    editing?.let { svc ->
        EditServiceDialog(
            service = svc,
            categories = categories,
            onDismiss = { editing = null },
            onSave = { title, desc, min, max, catId ->
                scope.launch {
                    message = null
                    val updated = catalogRepo.updateService(svc.id, title, desc, min, max, catId)
                    editing = null
                    if (updated != null) refreshKey++
                    else message = "No se pudo guardar los cambios (revisa SERVICE_DEBUG)."
                }
            }
        )
    }
}

@Composable
private fun ServiceManageCard(
    service: ServiceItem,
    onEdit: () -> Unit,
    onToggle: () -> Unit
) {
    val active = service.isActive()
    val titleColor = if (active) DarkGray else Color.Gray
    val cardColor = if (active) Color.White else Color(0xFFEDEDED)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (active) 2.dp else 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = service.title.ifBlank { "Service #${service.id}" },
                    fontWeight = FontWeight.Bold, fontSize = 16.sp, color = titleColor,
                    modifier = Modifier.weight(1f)
                )
                StatusPill(active = active)
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (service.categoryName != null) {
                Text(service.categoryName, fontSize = 13.sp, color = if (active) Primary else Color.Gray, fontWeight = FontWeight.Medium)
            }
            if (service.description.isNotBlank()) {
                Text(service.description, fontSize = 13.sp, color = Color.Gray, maxLines = 2)
            }
            val min = service.minimumPrice
            val max = service.maximumPrice
            if (min != null && max != null) {
                Text("S/ ${min.toInt()} - S/ ${max.toInt()}", fontSize = 13.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onEdit,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit", fontSize = 13.sp)
                }
                OutlinedButton(
                    onClick = onToggle,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (active) Color(0xFF9A6A00) else Color(0xFF2E7D32)
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(if (active) "Deactivate" else "Activate", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun StatusPill(active: Boolean) {
    val bg = if (active) Color(0xFFE1F5EE) else Color(0xFFE0E0E0)
    val fg = if (active) Color(0xFF0F6E56) else Color(0xFF616161)
    Surface(shape = RoundedCornerShape(99.dp), color = bg) {
        Text(
            if (active) "ACTIVE" else "INACTIVE",
            fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = fg,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EditServiceDialog(
    service: ServiceItem,
    categories: List<ServiceCategory>,
    onDismiss: () -> Unit,
    onSave: (String, String, Double, Double, Long) -> Unit
) {
    var title by remember { mutableStateOf(service.title) }
    var description by remember { mutableStateOf(service.description) }
    var priceMin by remember { mutableStateOf(service.minimumPrice?.toInt()?.toString() ?: "") }
    var priceMax by remember { mutableStateOf(service.maximumPrice?.toInt()?.toString() ?: "") }
    var selectedCategoryId by remember { mutableStateOf(service.categoryId) }

    val minV = priceMin.toDoubleOrNull()
    val maxV = priceMax.toDoubleOrNull()
    val valid = title.isNotBlank() && description.isNotBlank() &&
            minV != null && maxV != null && minV <= maxV && selectedCategoryId != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar servicio", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text("Título") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description, onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth().height(90.dp), shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceMin,
                        onValueChange = { priceMin = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Min S/") }, singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = priceMax,
                        onValueChange = { priceMax = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Max S/") }, singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Categoría", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DarkGray)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = cat.id == selectedCategoryId,
                            onClick = { selectedCategoryId = cat.id },
                            label = { Text(cat.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val catId = selectedCategoryId
                    if (valid && catId != null && minV != null && maxV != null) {
                        onSave(title.trim(), description.trim(), minV, maxV, catId)
                    }
                },
                enabled = valid
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
