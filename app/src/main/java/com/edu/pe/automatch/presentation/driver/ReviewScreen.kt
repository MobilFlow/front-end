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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pe.automatch.di.RepositoryModule
import com.edu.pe.automatch.presentation.theme.AccentBlue
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.SoftBackground
import kotlinx.coroutines.launch

@Composable
fun ReviewScreen(
    serviceId: String = "",
    onPublish: () -> Unit = {}
) {
    var loading by remember { mutableStateOf(true) }
    var mechanicName by remember { mutableStateOf("") }
    var serviceDescription by remember { mutableStateOf("") }
    var mechanicInitials by remember { mutableStateOf("") }
    var mechanicProfileId by remember { mutableStateOf<Long?>(null) }
    var driverProfileId by remember { mutableStateOf<Long?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var submitError by remember { mutableStateOf(false) }

    var rating by remember { mutableStateOf(0) }
    var alreadyReviewed by remember { mutableStateOf(false) }
    var comment by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val userRepo = remember { RepositoryModule.provideUserRepository() }
    val driverRepo = remember { RepositoryModule.provideDriverRepository() }
    val mechanicRepo = remember { RepositoryModule.provideMechanicRepository() }
    val serviceRequestRepo = remember { RepositoryModule.provideServiceRequestRepository() }
    val reviewRepo = remember { RepositoryModule.provideReviewRepository() }

    LaunchedEffect(serviceId) {
        try {
            val currentUser = userRepo.getCurrentUser()
            val dp = if (currentUser != null) driverRepo.getDriverByUserId(currentUser.id) else null
            driverProfileId = dp?.id

            val sid = serviceId.toLongOrNull()
            if (sid != null) {
                val sr = serviceRequestRepo.getServiceRequestById(sid)
                if (sr != null) {
                    serviceDescription = sr.description ?: "Service completed"

                    val mpId = sr.mechanicProfileId
                    if (mpId != null) {
                        mechanicProfileId = mpId
                        val allMechanics = mechanicRepo.getAllMechanics()
                        val mp = allMechanics.find { it.id == mpId }
                        if (mp != null) {
                            val mechUser = userRepo.getUserById(mp.userId)
                            val name = mechUser?.fullName ?: mp.workshopName ?: "Mechanic"
                            mechanicName = name
                            mechanicInitials = name.split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }
                        }

                        // ¿Este driver ya dejó una reseña para este servicio?
                        val dpId = dp?.id
                        if (dpId != null) {
                            alreadyReviewed = reviewRepo.getMechanicReviews(mpId)
                                .any { it.driverId == dpId && it.serviceId == sid }
                        }
                    }
                }
            }
        } catch (_: Exception) {
        }
        loading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            Spacer(modifier = Modifier.height(80.dp))
            CircularProgressIndicator(color = Primary)
        } else {
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(mechanicInitials.ifEmpty { "?" }, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = mechanicName.ifEmpty { "Mechanic" },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = DarkGray
                        )
                        Text(
                            text = serviceDescription,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.Build,
                        contentDescription = null,
                        tint = AccentBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (alreadyReviewed) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Ya dejaste una reseña para este servicio.",
                    fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = DarkGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Solo puedes calificar una vez por servicio.",
                    fontSize = 13.sp, color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onPublish,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(text = "Go back", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            } else {

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "How did it go?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star ${index + 1}",
                            tint = if (index < rating) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier
                                .clickable { rating = index + 1 }
                                .size(44.dp)
                                .background(
                                    color = if (index < rating) Color(0xFFFFC107).copy(alpha = 0.1f)
                                    else Color.Transparent,
                                    shape = CircleShape
                                )
                                .padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    shape = RoundedCornerShape(16.dp),
                    placeholder = { Text("Share your experience...", color = Color.LightGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = Primary
                    )
                )

                if (submitError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Failed to submit review. Try again.", color = Color.Red, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        scope.launch {
                            isSubmitting = true
                            submitError = false
                            try {
                                val sid = serviceId.toLongOrNull() ?: return@launch
                                val mpId = mechanicProfileId ?: return@launch
                                val dpId = driverProfileId ?: return@launch

                                if (comment.isNotBlank()) {
                                    reviewRepo.createReview(comment, mpId, dpId, sid, true)
                                }
                                if (rating > 0) {
                                    reviewRepo.createRating(rating, mpId, dpId, sid, true)
                                }
                                isSubmitting = false
                                onPublish()
                            } catch (_: Exception) {
                                isSubmitting = false
                                submitError = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        disabledContainerColor = Color.LightGray
                    ),
                    enabled = (rating > 0 || comment.isNotBlank()) && !isSubmitting
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = "Publish",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    AutoMatchTheme {
        ReviewScreen()
    }
}
