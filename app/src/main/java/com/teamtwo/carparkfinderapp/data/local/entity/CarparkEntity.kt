package com.teamtwo.carparkfinderapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teamtwo.carparkfinderapp.domain.model.Carpark

// android room database entity for the Carpark data, with coords converted to latlng
// we can use the _id field as the primary key, as they are unique

@Entity
data class CarparkEntity(
    @PrimaryKey val id: Int,
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
) {
    // helper function to convert database entity back to domain layer Carpark model
    fun toCarpark(): Carpark {
        return Carpark(
            id = id,
            carParkNo = carParkNo,
            address = address,
            lat = lat,
            lng = lng,
            parkingSystem = parkingSystem,
            carParkType = carParkType,
            carParkDecks = carParkDecks,
            gantryHeight = gantryHeight,
            shortTermParking = shortTermParking,
            hasNightParking = hasNightParking,
            freeParking = freeParking,
            isBookmarked = isBookmarked
        )
    }
}