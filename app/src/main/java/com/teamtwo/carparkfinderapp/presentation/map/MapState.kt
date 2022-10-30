package com.teamtwo.carparkfinderapp.presentation.map

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

// state wrapper - states here are not so frequently changed

data class MapState (
    val properties: MapProperties = MapProperties(
        isMyLocationEnabled = true
    ),
    val uiSettings: MapUiSettings = MapUiSettings(
        mapToolbarEnabled = true,
        myLocationButtonEnabled = true,
        zoomControlsEnabled = false
    ),
    val isBookmarkView: Boolean = false
)