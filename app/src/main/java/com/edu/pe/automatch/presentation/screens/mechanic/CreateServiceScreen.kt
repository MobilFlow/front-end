package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateServiceScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val mechanicRepo = remember { RepositoryModule.provideMechanicRepository() }
    val catalogRepo = remember { RepositoryModule.provideServiceCatalogRepository() }

    var mechanicProfileId by remember { mutableStateOf<Long?>(null) }
    var categories by remember { mutableStateOf<List<ServiceCategory>>(emptyList()) }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var newCategoryName by remember { mutableStateOf("") }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priceMin by remember { mutableStateOf("") }
    var priceMax by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var messageIsError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val user = userRepo.getCurrentUser()
            if (user != null) {
                mechanicProfileId = mechanicRepo.getMechanicByUserId(user.id)?.id
            }
            categories = catalogRepo.getCategories()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    fun createCategory() {
        val name = newCategoryName.trim()
        if (name.isBlank()) return
        scope.launch {
            val created = catalogRepo.createCategory(name)
            if (created != null) {
                categories = catalogRepo.getCategories()
                selectedCategoryId = created.id
                newCategoryName = ""
            } else {
                messageIsError = true
                message = "No se pudo crear la categoría."
            }
        }
    }

    val priceMinValue = priceMin.toDoubleOrNull()
    val priceMaxValue = priceMax.toDoubleOrNull()
    val isFormValid = title.isNotBlank() &&
            description.isNotBlank() &&
            priceMinValue != null &&
            priceMaxValue != null &&
            priceMinValue <= priceMaxValue &&
            selectedCategoryId != null &&
            mechanicProfileId != null

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkGray)
                }
                Text("Publish Service", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkGray)
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(SoftBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                if (mechanicProfileId == null) {
                    Text(
                        "No se encontró tu perfil de mecánico. Completa tu perfil antes de publicar.",
                        color = Color.Red, fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text("Title", fontWeight = FontWeight.SemiBold, color = DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("Ej: Cambio de llantas") },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Description", fontWeight = FontWeight.SemiBold, color = DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth().height(110.dp),
                    placeholder = { Text("Describe el servicio...") },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Min Price (S/)", fontWeight = FontWeight.SemiBold, color = DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = priceMin,
                            onValueChange = { priceMin = it.filter { c -> c.isDigit() || c == '.' } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { Text("10") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Max Price (S/)", fontWeight = FontWeight.SemiBold, color = DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = priceMax,
                            onValueChange = { priceMax = it.filter { c -> c.isDigit() || c == '.' } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { Text("20") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text("Category", fontWeight = FontWeight.SemiBold, color = DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                if (categories.isEmpty()) {
                    Text("No hay categorías aún. Crea una abajo.", fontSize = 13.sp, color = Color.Gray)
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = cat.id == selectedCategoryId,
                            onClick = { selectedCategoryId = cat.id },
                            label = { Text(cat.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        label = { Text("New category") },
                        placeholder = { Text("Ej: frenos") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { createCategory() },
                        enabled = newCategoryName.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }

                if (message != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(message!!, color = if (messageIsError) Color.Red else Color(0xFF2E7D32))
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        val mId = mechanicProfileId
                        val catId = selectedCategoryId
                        val min = priceMinValue
                        val max = priceMaxValue
                        if (mId == null || catId == null || min == null || max == null) return@Button
                        scope.launch {
                            isSaving = true
                            message = null
                            try {
                                val published = catalogRepo.publishService(
                                    mechanicProfileId = mId,
                                    title = title.trim(),
                                    description = description.trim(),
                                    priceMin = min,
                                    priceMax = max,
                                    categoryId = catId
                                )
                                isSaving = false
                                if (published != null) {
                                    navController.popBackStack()
                                } else {
                                    messageIsError = true
                                    message = "No se pudo publicar el servicio. Revisa el log SERVICE_DEBUG."
                                }
                            } catch (e: Exception) {
                                isSaving = false
                                messageIsError = true
                                message = "Error: ${e.message}"
                            }
                        }
                    },
                    enabled = isFormValid && !isSaving,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(if (isSaving) "Publishing..." else "Publish Service", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
