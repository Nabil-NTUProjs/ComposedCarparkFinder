package com.teamtwo.carparkfinderapp.domain.usecase

import com.teamtwo.carparkfinderapp.domain.model.Availability
import com.teamtwo.carparkfinderapp.domain.repository.AvailabilityRepository
import com.teamtwo.carparkfinderapp.util.Resource

class GetAvailability(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(cparkNo: String): Resource<List<Availability>> {
         return repository.getAvailability(cparkNo)
    }
}