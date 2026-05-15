package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme

private val PurpleBg       = Color(0xFFEEEDFE)
private val PurplePrimary  = Color(0xFF534AB7)
private val PurpleDark     = Color(0xFF26215C)
private val CardBackground = Color.White
private val ChipBg         = Color(0xFFE5E3F8)
private val SoftGray       = Color(0xFFF4F4F4)

@Composable
fun MechanicRequestsScreen(
    navController: NavController
) {

    var selectedTab by remember {
        mutableStateOf("accepted")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBg)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Requests",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleDark
            )

            Spacer(modifier = Modifier.height(20.dp))

            RequestTabs(
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedTab == "accepted") {

                AcceptedRequestCard(navController)

            } else {

                InProgressRequestCard(navController)
            }
        }

        BottomNavBar(
            selectedItem = 1,
            navType = BottomNavType.MECHANIC,

            onItemSelected = { index ->

                when(index) {

                    0 -> {
                        navController.navigate(
                            Screen.MechanicDashboard.route
                        )
                    }

                    1 -> {
                        navController.navigate(
                            Screen.MechanicRequests.route
                        )
                    }

                    2 -> {
                        navController.navigate(
                            Screen.MechanicHistory.route
                        )
                    }

                    3 -> {
                        navController.navigate(
                            Screen.MechanicProfile.route
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun RequestTabs(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White.copy(alpha = 0.7f),
                RoundedCornerShape(20.dp)
            )
            .padding(6.dp)
    ) {

        TabButton(
            title = "In progress",
            selected = selectedTab == "progress",
            modifier = Modifier.weight(1f)
        ) {
            onTabSelected("progress")
        }

        TabButton(
            title = "Review",
            selected = selectedTab == "accepted",
            modifier = Modifier.weight(1f)
        ) {
            onTabSelected("accepted")
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) PurplePrimary else Color.Transparent,
            contentColor = if (selected) Color.White else PurpleDark
        ),
        elevation = null
    ) {

        Text(
            text = title,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DriverProfileBanner(
    name: String,
    vehicle: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        PurplePrimary,
                        RoundedCornerShape(18.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = name.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = vehicle,
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Tap to view driver profile",
                    fontSize = 12.sp,
                    color = PurplePrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun AcceptedRequestCard(
    navController: NavController
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Brake inspection",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleDark
            )

            Spacer(modifier = Modifier.height(18.dp))

            DriverProfileBanner(
                name = "Carolina Herrera",
                vehicle = "Toyota Tundra 2026",

                onClick = {

                    navController.navigate(
                        Screen.DriverProfile.route
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Carolina Herrera",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Today 3pm",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Toyota Tundra 2026",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = "Estimate Quote",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    QuoteChip("$100")

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Date",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    QuoteChip("20/05/26")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Observations:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Cuando freno hay un sonido muy fuerte",
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Comments",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Write comments...")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(18.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary
                )
            ) {

                Text(
                    text = "Send Quote",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun InProgressRequestCard(
    navController: NavController
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Oil Change",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleDark
            )

            Spacer(modifier = Modifier.height(18.dp))

            DriverProfileBanner(
                name = "Jorge Ramírez",
                vehicle = "Mazda CX-3",

                onClick = {

                    navController.navigate(
                        Screen.DriverProfile.route
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Jorge Ramírez",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "May 1, 3pm",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Mazda CX-3",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text = "Final Quote",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    QuoteChip("$50")

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Date",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    QuoteChip("04/05/26")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Observations:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "None...",
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Comments",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(18.dp),
                color = SoftGray
            ) {

                Box(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Cambio hecho y pago recibido...",
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurplePrimary
                )
            ) {

                Text(
                    text = "Finish",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuoteChip(
    text: String
) {

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = ChipBg
    ) {

        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 22.dp,
                vertical = 12.dp
            ),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MechanicRequestsScreenPreview() {

    AutoMatchTheme {

        MechanicRequestsScreen(
            rememberNavController()
        )
    }
}