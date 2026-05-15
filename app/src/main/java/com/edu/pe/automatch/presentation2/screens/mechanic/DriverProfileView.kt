package com.edu.pe.automatch.presentation.screens.mechanic


import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun DriverProfileView() {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
            .verticalScroll(scrollState)
    ) {

        // HEADER
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(PrimaryDark)
            )

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Primary)
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "CH",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        // DRIVER INFO
        Text(
            text = "Carolina Herrera",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Lima, Peru",
            fontSize = 15.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // STATS
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),

            shape = RoundedCornerShape(18.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),

                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ProfileStat(
                    label = "Services",
                    value = "14"
                )

                ProfileStat(
                    label = "Rating",
                    value = "4.9"
                )

                ProfileStat(
                    label = "Vehicles",
                    value = "2"
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // VEHICLES
        Text(
            text = "Vehicles",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        VehicleCard(
            vehicle = "Toyota Tundra 2026",
            plate = "ABC-123"
        )

        VehicleCard(
            vehicle = "Mazda CX-3",
            plate = "LMN-982"
        )

        Spacer(modifier = Modifier.height(28.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(28.dp))

        // HISTORY
        Text(
            text = "Service History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        HistoryCard(
            service = "Brake Inspection",
            price = "$120",
            date = "May 10, 2026"
        )

        HistoryCard(
            service = "Oil Change",
            price = "$45",
            date = "Apr 28, 2026"
        )

        HistoryCard(
            service = "AC Repair",
            price = "$200",
            date = "Mar 15, 2026"
        )

        Spacer(modifier = Modifier.height(28.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(28.dp))

        // REVIEWS
        Text(
            text = "Reviews",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        ReviewCard(
            mechanicName = "Carlos M.",
            comment = "Very responsible client and easy communication.",
            rating = 5
        )

        ReviewCard(
            mechanicName = "Jorge R.",
            comment = "Vehicle was exactly as described.",
            rating = 4
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileStat(
    label: String,
    value: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun VehicleCard(
    vehicle: String,
    plate: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = vehicle,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = DarkGray
                )

                Text(
                    text = "Driver vehicle",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            SuggestionChip(
                onClick = {},

                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = AccentBlue.copy(alpha = 0.1f),
                    labelColor = Primary
                ),

                border = null,

                label = {

                    Text(
                        text = plate,
                        fontSize = 12.sp
                    )
                }
            )
        }
    }
}

@Composable
private fun HistoryCard(
    service: String,
    price: String,
    date: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = service,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = DarkGray
                )

                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            SuggestionChip(
                onClick = {},

                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Primary.copy(alpha = 0.1f),
                    labelColor = Primary
                ),

                border = null,

                label = {

                    Text(
                        text = price,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    }
}

@Composable
private fun ReviewCard(
    mechanicName: String,
    comment: String,
    rating: Int
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = mechanicName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = DarkGray
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    repeat(rating) {

                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = comment,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(
                onClick = {},
                modifier = Modifier.padding(0.dp),
                contentPadding = ButtonDefaults.TextButtonContentPadding
            ) {

                Text(
                    text = "Read More",
                    fontSize = 12.sp,
                    color = PrimaryLight
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DriverProfileViewPreview() {

    AutoMatchTheme {

        DriverProfileView()
    }
}