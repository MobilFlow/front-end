// BottomNavBar.kt
package com.edu.pe.automatch.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class BottomNavType {
    DRIVER,
    MECHANIC
}

@Composable
fun BottomNavBar(
    selectedItem: Int,
    navType: BottomNavType,
    onItemSelected: (Int) -> Unit
) {

    NavigationBar {

        // HOME / DASHBOARD
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    if (navType == BottomNavType.DRIVER)
                        "Home"
                    else
                        "Dashboard"
                )
            }
        )

        // SEARCH / REQUESTS
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    if (navType == BottomNavType.DRIVER)
                        "Search"
                    else
                        "Requests"
                )
            }
        )

        // HISTORY / SERVICES
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            icon = {
                Icon(
                    imageVector = if (navType == BottomNavType.DRIVER)
                        Icons.Default.History
                    else
                        Icons.Default.Build,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    if (navType == BottomNavType.DRIVER)
                        "History"
                    else
                        "Services"
                )
            }
        )

        // PROFILE
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = { onItemSelected(3) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            label = {
                Text("Profile")
            }
        )
    }
}