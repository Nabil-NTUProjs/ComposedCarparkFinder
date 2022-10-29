package com.teamtwo.carparkfinderapp.domain.repository

import com.teamtwo.carparkfinderapp.domain.model.Availability
import com.teamtwo.carparkfinderapp.util.Resource

interface AvailabilityRepository {
    suspend fun getAvailability(cparkNo: String): Resource<List<Availability>>
}