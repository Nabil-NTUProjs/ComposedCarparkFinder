package com.teamtwo.carparkfinderapp.domain.usecase

import com.teamtwo.carparkfinderapp.domain.model.Carpark
import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository
import com.teamtwo.carparkfinderapp.util.Resource

class GetBookmarkedCarparks(
    private val repository: CarparkRepository){
    suspend operator fun invoke() : Resource<List<Carpark>> {
        return repository.getBookmarkedCarparks()
    }
}