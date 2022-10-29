package com.teamtwo.carparkfinderapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teamtwo.carparkfinderapp.data.local.entity.CarparkEntity

// room database class - uses carpark dao to make queries

@Database(
    entities = [CarparkEntity::class],
    version = 1
)
abstract class CarparkDatabase: RoomDatabase() {

    abstract val dao: CarparkDao
}