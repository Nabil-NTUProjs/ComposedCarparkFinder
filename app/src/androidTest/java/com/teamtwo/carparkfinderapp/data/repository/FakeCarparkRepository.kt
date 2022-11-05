package com.teamtwo.carparkfinderapp.data.repository

import com.teamtwo.carparkfinderapp.domain.model.Carpark
import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository
import com.teamtwo.carparkfinderapp.util.Resource

class FakeCarparkRepository : CarparkRepository {

    // dummy carparks to initialise the state
    private val dummyCarpark = Carpark(
        1,
        "LOL",
        "THIS CAR PARK DOES NOT EXIST",
        1.3010626054202958,
        103.85411771659147,
        "ELECTRONIC PARKING",
        "BASEMENT CAR PARK",
        1,
        1.0.toFloat(),
        "NEVER",
        "NO",
        "NOPE",
        0
    )

    private val dummyCarpark2 = Carpark(
        2,
        "YESSIR",
        "THIS CAR PARK DOES NOT EXIST",
        1.3010626054202958,
        103.85411771659147,
        "ELECTRONIC PARKING",
        "BASEMENT CAR PARK",
        1,
        1.0.toFloat(),
        "YUP!",
        "YA",
        "YES!",
        1
    )

    private val carparkItems = buildList<Carpark> {
        add(0, dummyCarpark)
        add(1, dummyCarpark2    )
    }

    private var shouldReturnNetworkError = -1

    /*
    Pass in 0 to simulate a HTTPException
    Pass in 1 to simulate an IOException
     */
    fun setShouldReturnNetworkError(value: Int) {
        shouldReturnNetworkError = value
    }

    override suspend fun getCarparks(): Resource<List<Carpark>> {

        return when (shouldReturnNetworkError) {
            0 -> {
                Resource.Error("Something went wrong.", null)
            }
            1 -> {
                Resource.Error("Couldn't reach server, check your internet connection.", null)
            }
            else -> {
                // we assume that mapping to Carpark from CarparkEntity has been done
                Resource.Success(carparkItems)
            }
        }
    }

    override suspend fun getBookmarkedCarparks(): Resource<List<Carpark>> {
        return when (shouldReturnNetworkError) {
            0 -> {
                Resource.Error("Something went wrong.", null)
            }
            1 -> {
                Resource.Error("Couldn't reach server, check your internet connection.", null)
            }
            else -> {
                // return bookmarked items only
                carparkItems.filter {
                    it.isBookmarked == 1
                }
                // we assume that mapping to Carpark from CarparkEntity has been done
                Resource.Success(carparkItems)
            }
        }
    }

    override suspend fun updateBookmark(flag: Int, id: Int): Boolean {
        TODO("Not yet implemented")
    }
}