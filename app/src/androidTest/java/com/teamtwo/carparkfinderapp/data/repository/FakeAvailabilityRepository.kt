package com.teamtwo.carparkfinderapp.data.repository

import com.teamtwo.carparkfinderapp.domain.model.Availability
import com.teamtwo.carparkfinderapp.domain.repository.AvailabilityRepository
import com.teamtwo.carparkfinderapp.util.Resource

class FakeAvailabilityRepository : AvailabilityRepository {

    private val availabilityItems = mutableListOf<Availability>()
    private var shouldReturnNetworkError = -1

    /*
    Pass in 0 to simulate a HTTPException
    Pass in 1 to simulate an IOException
     */
    fun setShouldReturnNetworkError(value: Int) {
        shouldReturnNetworkError = value
    }

    override suspend fun getAvailability(cparkNo: String): Resource<List<Availability>> {

        return when (shouldReturnNetworkError) {
            0 -> {
                Resource.Error("Something went wrong.", null)
            }
            1 -> {
                Resource.Error("Couldn't reach server, check your internet connection.", null)
            }
            else -> {
                Resource.Success(availabilityItems)
            }
        }
    }
}