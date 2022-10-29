package com.teamtwo.carparkfinderapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teamtwo.carparkfinderapp.data.local.entity.CarparkEntity

// data access object - define functions used to access the database

@Dao
interface CarparkDao {
    // database operations should be suspend functions as they are long-running

    // insert multiple CarparkEntity objects at once, ignoring entries if they are the same (no overwrite)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCarparks(cparks : List<CarparkEntity>)

    // set/unset bookmark flag
    @Query("UPDATE carparkentity SET isBookmarked=:flag WHERE id=:id")
    suspend fun updateBookmark(flag: Int, id: Int)

    // pass a list of primary keys, and the corresponding entities will be deleted
    @Query("DELETE FROM carparkentity WHERE carParkNo IN(:carParkNoList)")
    suspend fun deleteCarparks(carParkNoList: List<String>)

    // get the whole list of entities from the database
    @Query("SELECT * FROM carparkentity")
    suspend fun getCarparks(): List<CarparkEntity>

    // get list of entities where bookmark flag is set
    @Query("SELECT * FROM carparkentity WHERE isBookmarked=1")
    suspend fun getBookmarkedCarparks(): List<CarparkEntity>

}