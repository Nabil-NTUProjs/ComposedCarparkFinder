package com.teamtwo.carparkfinderapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teamtwo.carparkfinderapp.data.local.entity.AvailabilityEntity

@Database(
    entities = [AvailabilityEntity::class],
    version = 1
)
abstract class AvailabilityDatabase: RoomDatabase() {

    abstract val dao: AvailabilityDao
}