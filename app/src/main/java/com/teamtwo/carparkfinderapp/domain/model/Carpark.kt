package com.teamtwo.carparkfinderapp.domain.model


data class Carpark(
    val id: Int,
    val carParkNo: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val parkingSystem: String,
    val carParkType: String,
    val carParkDecks: Int,
    val gantryHeight: Float,
    val shortTermParking: String,
    val hasNightParking: String,
    val freeParking: String,
    val isBookmarked: Int
)
