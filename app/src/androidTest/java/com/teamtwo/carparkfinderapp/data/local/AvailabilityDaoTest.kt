package com.teamtwo.carparkfinderapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.teamtwo.carparkfinderapp.data.local.entity.AvailabilityEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// unit test for the DAO (local database)
@RunWith(AndroidJUnit4::class)
@SmallTest
class AvailabilityDaoTest {

    private lateinit var database: AvailabilityDatabase
    private lateinit var dao: AvailabilityDao

    @Before
    fun setup() {
        // non-persistent database instance for test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AvailabilityDatabase::class.java
        ).allowMainThreadQueries().build()  // tests should be independent and single-threaded

        dao = database.dao
    }

    @After
    fun teardown() {
        database.close()
    }

    // test the getAvailability functionality
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAvailabilityTest() = runTest {
        // create and insert a test item
        val availabilityItem = AvailabilityEntity(
            carparkNumber = "U69",
            updateDateTime = "Current Time",
            lotType = "C",
            totalLots = 60,
            availableLots = 20
        )
        val availabilityList = listOf<AvailabilityEntity>(availabilityItem)
        dao.insertAvailabilities(availabilityList)

        val returnedAvailabilities = dao.getAvailability("U69")
        // check if the returned value is the one we inserted
        assertThat(returnedAvailabilities).contains(availabilityItem)
    }
}