// BottomNavBar.kt
package com.edu.pe.automatch.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavType {
    DRIVER,
    MECHANIC
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(
    selectedItem: Int,
    navType: BottomNavType,
    onItemSelected: (Int) -> Unit
) {

    val items = when (navType) {
        BottomNavType.DRIVER -> listOf(
            BottomNavItem("Home", Icons.Default.Home),
            BottomNavItem("Search", Icons.Default.Search),
            BottomNavItem("History", Icons.Default.History),
            BottomNavItem("Profile", Icons.Default.Person)
        )

        BottomNavType.MECHANIC -> listOf(
            BottomNavItem("Dashboard", Icons.Default.Home),
            BottomNavItem("Requests", Icons.Default.Search),
            BottomNavItem("Services", Icons.Default.Build),
            BottomNavItem("Profile", Icons.Default.Person)
        )
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}