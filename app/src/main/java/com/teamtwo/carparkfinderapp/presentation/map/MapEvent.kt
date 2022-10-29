package com.teamtwo.carparkfinderapp.presentation.map

import com.google.android.gms.maps.model.LatLng
import com.teamtwo.carparkfinderapp.domain.model.Carpark

sealed class MapEvent {
    // on clicking the bookmark view FAB
    object ToggleBookmarkView: MapEvent()

    // on long clicking a coordinate on the map
    data class OnMapLongClick(val latLng: LatLng): MapEvent()

    // on long clicking a carpark object on the map, open the detail screen for the object
    data class InfoWindowLongClick(val cpark: Carpark): MapEvent()
}