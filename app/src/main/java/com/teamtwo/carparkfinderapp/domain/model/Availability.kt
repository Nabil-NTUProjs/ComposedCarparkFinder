package com.teamtwo.carparkfinderapp.domain.model

data class Availability (
    val carparkNumber: String,
    val updateDateTime: String,
    val lotType: String,
    val totalLots: Int,
    val availableLots: Int
)