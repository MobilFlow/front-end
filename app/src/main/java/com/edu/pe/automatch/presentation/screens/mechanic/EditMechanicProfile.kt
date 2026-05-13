// EditMechanicProfile.kt
package com.edu.pe.automatch.presentation.screens.mechanic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edu.pe.automatch.presentation.components.BottomNavBar
import com.edu.pe.automatch.presentation.components.ProfileHeader
import com.edu.pe.automatch.presentation.components.SpecialtyChip
import com.edu.pe.automatch.presentation.theme.AutoMatchTheme
import com.edu.pe.automatch.presentation.theme.SoftBackground

@Composable
fun EditMechanicProfile() {

    val description = remember {
        mutableStateOf(
            "Certified mechanic with 8 years of experience."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBackground)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            item {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    ProfileHeader(
                        name = "Jorge Ramirez",
                        location = "Certified Mechanic - Callao, Peru"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Presentation",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description.value,
                        onValueChange = {
                            description.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Specialties",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        SpecialtyChip(label = "Brakes")
                        SpecialtyChip(label = "Engine")
                        SpecialtyChip(label = "Transmission")
                        SpecialtyChip(label = "SUV")
                        SpecialtyChip(label = "Sedan")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = "La Perla Callao 333",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = "Monday to Friday · 7:00am - 8:00pm",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = {}
                    ) {
                        Text("Save")
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        BottomNavBar(
            selectedItem = 3,
            onItemSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditMechanicProfilePreview() {
    AutoMatchTheme {
        EditMechanicProfile()
    }
}