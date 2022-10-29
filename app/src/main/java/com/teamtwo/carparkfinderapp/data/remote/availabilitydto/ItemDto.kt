package com.teamtwo.carparkfinderapp.data.remote.availabilitydto

data class ItemDto(
    val carpark_data: List<CarparkDataDto>,
    val timestamp: String
)