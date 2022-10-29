package com.teamtwo.carparkfinderapp.domain.usecase

import com.teamtwo.carparkfinderapp.domain.model.Carpark
import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository
import com.teamtwo.carparkfinderapp.util.Resource

// for the user (presentation end) to refresh the database by getting all carparks again

class GetCarparks(
    private val repository: CarparkRepository
) {
    // with this, the use case class may be called as a function
    // viewModel will call this use case and present data to the ui

    suspend operator fun invoke() : Resource<List<Carpark>> {
        return repository.getCarparks()
    }
}