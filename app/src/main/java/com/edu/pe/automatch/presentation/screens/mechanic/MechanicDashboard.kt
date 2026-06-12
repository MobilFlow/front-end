package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.BottomNavType
import com.edu.pe.automatch.presentation.navigation.Screen
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme

// ── Colors ────────────────────────────────────────────────────────────────────
private val PurpleBg       = Color(0xFFEEEDFE)
private val DarkCardBg     = Color(0xFF1A1740)
private val DarkCardBadge  = Color(0xFF2D2A5E)
private val PurplePrimary  = Color(0xFF534AB7)
private val PurpleMid      = Color(0xFF7F77DD)
private val PurpleLight    = Color(0xFFAFA9EC)
private val PurpleDark     = Color(0xFF26215C)
private val GoldAccent     = Color(0xFFFAC775)
private val GreenBadgeBg   = Color(0xFFE1F5EE)
private val GreenBadgeText = Color(0xFF0F6E56)

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun MechanicDashboard(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleBg)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 28.dp, bottom = 12.dp),

            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            HeaderRow()

            StatsCard()

            QuickActionsRow(navController)

            SectionWithSeeAll(
                title = "Requests",
                showSeeAll = true
            )

            RequestCard(
                name = "Carolina Herrera",
                vehicle = "Toyota Tundra 2026",
                service = "Brakes",
                distance = "12 km",
                rating = "4.6",
                badgeLabel = "New",
                badgeIsPurple = true
            )

            SectionWithSeeAll(
                title = "In Progress",
                showSeeAll = false
            )

            InProgressCard(
                service = "Oil Change",
                client = "Juan Perez · Mazda",
                price = "$50",

                onClick = {

                    navController.navigate(
                        Screen.MechanicHistory.route
                    )
                }
            )
        }

        BottomNavBar(
            selectedItem = 0,
            navType = BottomNavType.MECHANIC,

            onItemSelected = { index ->

                when (index) {

                    0 -> {

                        navController.navigate(
                            Screen.MechanicDashboard.route
                        ) {
                            launchSingleTop = true
                        }
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

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun HeaderRow() {

    Row(
        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            Text(
                text = "Welcome Back,",
                fontSize = 15.sp,
                color = PurplePrimary
            )

            Text(
                text = "Jorge Ramirez",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = PurpleDark
            )
        }

        AvatarCircle(initials = "JR")
    }
}

@Composable
private fun AvatarCircle(
    initials: String
) {

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color(0xFF3C3489)),

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = initials,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = PurpleBg
        )
    }
}

// ── Stats Card ────────────────────────────────────────────────────────────────
@Composable
private fun StatsCard() {

    Card(
        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = DarkCardBg
        ),

        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "My Services",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkCardBadge
                ) {

                    Text(
                        text = "Mechanic",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = PurpleLight,

                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 6.dp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                StatItem(
                    value = "1",
                    label = "Requests",
                    valueColor = PurpleLight,
                    modifier = Modifier.weight(1f)
                )

                StatDivider()

                StatItem(
                    value = "$800",
                    label = "This Week",
                    valueColor = Color.White,
                    modifier = Modifier.weight(1f)
                )

                StatDivider()

                StatItem(
                    value = "4.9",
                    label = "Rating",
                    valueColor = GoldAccent,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = PurpleMid
        )
    }
}

@Composable
private fun StatDivider() {

    Box(
        modifier = Modifier
            .width(0.5.dp)
            .height(44.dp)
            .background(Color(0xFF3C3489))
    )
}

// ── Quick Actions ─────────────────────────────────────────────────────────────
@Composable
private fun QuickActionsRow(
    navController: NavController
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        QuickActionCard(
            icon = Icons.Outlined.Build,
            title = "New Request",
            subtitle = "Review and accept",
            modifier = Modifier.weight(1f),

            onClick = {

                navController.navigate(
                    Screen.MechanicRequests.route
                )
            }
        )

        QuickActionCard(
            icon = Icons.Outlined.Schedule,
            title = "History",
            subtitle = "1 service active",
            modifier = Modifier.weight(1f),

            onClick = {

                navController.navigate(
                    Screen.MechanicHistory.route
                )
            }
        )
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PurplePrimary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ── Section Header ────────────────────────────────────────────────────────────
@Composable
private fun SectionWithSeeAll(
    title: String,
    showSeeAll: Boolean
) {

    Row(
        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = PurpleDark
        )

        if (showSeeAll) {

            Text(
                text = "See All",
                fontSize = 13.sp,
                color = PurplePrimary
            )
        }
    }
}

// ── Request Card ──────────────────────────────────────────────────────────────
@Composable
private fun RequestCard(
    name: String,
    vehicle: String,
    service: String,
    distance: String,
    rating: String,
    badgeLabel: String,
    badgeIsPurple: Boolean
) {

    Card(
        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                StatusBadge(
                    label = badgeLabel,
                    isPurple = badgeIsPurple
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = vehicle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(thickness = 0.5.dp)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                MetaChip(
                    icon = Icons.Outlined.Settings,
                    text = service
                )

                MetaChip(
                    icon = Icons.Outlined.LocationOn,
                    text = distance
                )

                MetaChip(
                    icon = Icons.Outlined.Star,
                    text = rating
                )
            }
        }
    }
}

// ── In Progress Card ──────────────────────────────────────────────────────────
@Composable
private fun InProgressCard(
    service: String,
    client: String,
    price: String,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = service,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                StatusBadge(
                    label = "On going",
                    isPurple = false
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = client,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(thickness = 0.5.dp)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                MetaChip(
                    icon = Icons.Outlined.AttachMoney,
                    text = price
                )

                MetaChip(
                    icon = Icons.Outlined.Schedule,
                    text = "In process"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Tap to open service history",
                fontSize = 12.sp,
                color = PurplePrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── Shared Components ─────────────────────────────────────────────────────────
@Composable
private fun StatusBadge(
    label: String,
    isPurple: Boolean
) {

    val bg = if (isPurple) {
        PurpleBg
    } else {
        GreenBadgeBg
    }

    val text = if (isPurple) {
        PurplePrimary
    } else {
        GreenBadgeText
    }

    Surface(
        shape = RoundedCornerShape(99.dp),
        color = bg
    ) {

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = text,

            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 3.dp
            )
        )
    }
}

@Composable
private fun MetaChip(
    icon: ImageVector,
    text: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ── Preview ───────────────────────────────────────────────────────────────────
@Preview(
    showBackground = true,
    backgroundColor = 0xFFEEEDFE
)
@Composable
fun MechanicDashboardPreview() {

    AutoMatchTheme {

        MechanicDashboard(
            rememberNavController()
        )
    }
}