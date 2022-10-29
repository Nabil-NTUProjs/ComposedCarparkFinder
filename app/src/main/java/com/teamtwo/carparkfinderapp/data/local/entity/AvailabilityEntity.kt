package com.teamtwo.carparkfinderapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teamtwo.carparkfinderapp.domain.model.Availability

@Entity
data class AvailabilityEntity(
    @PrimaryKey val carparkNumber: String,
    val updateDateTime: String,
    val lotType: String,
    val totalLots: Int,
    val availableLots: Int
) {
    // helper function to convert database entity back to domain layer Availability model
    fun toAvailability(): Availability {
        return Availability(
            carparkNumber = carparkNumber,
            updateDateTime = updateDateTime,
            lotType = lotType,
            totalLots = totalLots,
            availableLots = availableLots
        )
    }
}
