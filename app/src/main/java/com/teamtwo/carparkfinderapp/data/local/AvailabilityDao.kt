package com.teamtwo.carparkfinderapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teamtwo.carparkfinderapp.data.local.entity.AvailabilityEntity

@Dao
interface AvailabilityDao {
    // database operations should be suspend functions as they are long-running

    // insert multiple availabilityEntity objects at once, replacing existing entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvailabilities(availabilityList : List<AvailabilityEntity>)

    // get the whole list of entities from the database
    @Query("SELECT * FROM availabilityentity WHERE carparkNumber=:cparkNo")
    suspend fun getAvailability(cparkNo: String): List<AvailabilityEntity>
}