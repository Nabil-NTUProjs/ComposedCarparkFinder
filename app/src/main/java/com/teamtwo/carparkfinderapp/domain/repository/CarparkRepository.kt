package com.teamtwo.carparkfinderapp.domain.repository

import com.teamtwo.carparkfinderapp.domain.model.Carpark
import com.teamtwo.carparkfinderapp.util.Resource

// use cases for the controller / domain layer

interface CarparkRepository {
    suspend fun getCarparks(): Resource<List<Carpark>>
    suspend fun getBookmarkedCarparks(): Resource<List<Carpark>>
    suspend fun updateBookmark(flag: Int, id: Int): Boolean
}