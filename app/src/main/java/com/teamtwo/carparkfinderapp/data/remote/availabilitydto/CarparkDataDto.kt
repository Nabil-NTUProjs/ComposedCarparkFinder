package com.teamtwo.carparkfinderapp.data.remote.availabilitydto

data class CarparkDataDto(
    val carpark_info: List<CarparkInfoDto>,
    val carpark_number: String,
    val update_datetime: String
)