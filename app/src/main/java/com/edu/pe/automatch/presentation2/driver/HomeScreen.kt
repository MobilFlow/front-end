package com.edu.pe.automatch.presentation.driver

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edu.pe.automatch.presentation.theme.AccentBlue
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.DarkGray
import com.edu.pe.automatch.presentation.theme.Primary
import com.edu.pe.automatch.presentation.theme.PrimaryDark
import com.edu.pe.automatch.presentation.theme.PrimaryLight
import com.edu.pe.automatch.presentation.theme.SoftBackground

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMechanic: (String) -> Unit = {},
    onNavigateToReview: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Hello,", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Liam Parker",
                    color = DarkGray,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "LP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Vehicle Dashboard Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryDark),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Toyota Corolla",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    SuggestionChip(
                        onClick = {},
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = AccentBlue.copy(alpha = 0.2f),
                            labelColor = Color.White
                        ),
                        border = null,
                        label = { Text("ABC-123", fontSize = 12.sp, fontWeight = FontWeight.Medium) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "Active", value = "2", color = AccentBlue)
                    StatItem(label = "Total", value = "14", color = Color.White)
                    StatItem(label = "Rating", value = "4.8", color = Color(0xFFFFC107))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Build,
                title = "Request a service",
                subtitle = "Find the best mechanic",
                onClick = {}
            )
            QuickActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.History,
                title = "View history",
                subtitle = "14 services done",
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mechanics Nearby
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mechanics Nearby",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGray
            )
            TextButton(onClick = {}) {
                Text("See all", color = PrimaryLight)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mechanic cards
        val mechanics = listOf(
            MechanicData("Carlos M.", listOf("Engine", "Brakes"), listOf("Brakes", "AC")),
            MechanicData("Ana L.", listOf("Transmission", "Electrical"), listOf("Engine", "Battery")),
            MechanicData("Pedro R.", listOf("Suspension", "Tires"), listOf("Tires", "Alignment"))
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mechanics) { mechanic ->
                MechanicCard(
                    mechanic = mechanic,
                    onClick = { onNavigateToMechanic("1") }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkGray)
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

private data class MechanicData(
    val name: String,
    val specialties: List<String>,
    val tags: List<String>
)

@Composable
private fun MechanicCard(
    mechanic: MechanicData,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mechanic.name.take(2).replace(" ", ""),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = mechanic.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mechanic.specialties.joinToString(" • "),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                mechanic.tags.forEach { tag ->
                    SuggestionChip(
                        onClick = {},
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = AccentBlue.copy(alpha = 0.1f),
                            labelColor = Primary
                        ),
                        border = null,
                        modifier = Modifier.height(28.dp),
                        label = { Text(tag, fontSize = 11.sp) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AutoMatchTheme {
        HomeScreen()
    }
}
