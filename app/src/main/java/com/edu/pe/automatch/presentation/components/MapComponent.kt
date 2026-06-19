package com.edu.pe.automatch.presentation.components

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun MapComponent(
    latitude: Double? = null,
    longitude: Double? = null,
    address: String? = null,
    title: String? = null
) {
    val context = LocalContext.current

    // Coordenadas por defecto (Lima, Perú)
    val defaultLatLng = LatLng(-12.046374, -77.042793)
    
    var location by remember {
        mutableStateOf(
            if (latitude != null && longitude != null) LatLng(latitude, longitude)
            else defaultLatLng
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 14f)
    }

    val markerState = rememberMarkerState(position = location)

    // Sync when direct coordinates change
    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            val newLatLng = LatLng(latitude, longitude)
            location = newLatLng
            markerState.position = newLatLng
            cameraPositionState.position = CameraPosition.fromLatLngZoom(newLatLng, 15f)
        }
    }

    // Geocoding logic for address-based location
    LaunchedEffect(address) {
        if (!address.isNullOrBlank() && (latitude == null || longitude == null)) {
            withContext(Dispatchers.IO) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val results = geocoder.getFromLocationName("$address, Lima, Peru", 1)
                    if (!results.isNullOrEmpty()) {
                        val latLng = LatLng(results[0].latitude, results[0].longitude)
                        withContext(Dispatchers.Main) {
                            location = latLng
                            markerState.position = latLng
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    if (LocalInspectionMode.current) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Map, contentDescription = "Map Placeholder")
        }
    } else {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = markerState,
                title = title ?: address ?: "Workshop Location"
            )
        }
    }
}
