package com.teamtwo.carparkfinderapp.data.remote.dto

import com.teamtwo.carparkfinderapp.data.local.entity.CarparkEntity
import net.qxcg.svy21.SVY21Coordinate

// this represents the data retrieved by retrofit, which will be formatted for use in our application

data class CarparkDto(
    val _id: Int,
    val address: String,
    val car_park_basement: String,
    val car_park_decks: String,
    val car_park_no: String,
    val car_park_type: String,
    val free_parking: String,
    val gantry_height: String,
    val night_parking: String,
    val short_term_parking: String,
    val type_of_parking_system: String,
    val x_coord: String,
    val y_coord: String
) {

    // mapper to turn dto into a data class for our domain layer
    fun toCarparkEntity(): CarparkEntity {

        // call upon helper package to convert svy21 to lat and long
        val svyCoord = SVY21Coordinate(y_coord.toDouble(), x_coord.toDouble())
        val latlonCoord = svyCoord.asLatLon()

        return CarparkEntity(
            id = _id,
            carParkNo = car_park_no,
            address = address,
            lat = latlonCoord.latitude,
            lng = latlonCoord.longitude,
            parkingSystem = type_of_parking_system,
            carParkType = car_park_type,
            carParkDecks = car_park_decks.toInt(),
            gantryHeight = gantry_height.toFloat(),
            shortTermParking = short_term_parking,
            hasNightParking = night_parking,
            freeParking = free_parking,
            isBookmarked = 0
        )
    }
}
