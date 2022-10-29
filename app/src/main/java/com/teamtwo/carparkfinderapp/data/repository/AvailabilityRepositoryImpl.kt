package com.teamtwo.carparkfinderapp.data.repository

import com.teamtwo.carparkfinderapp.data.local.AvailabilityDao
import com.teamtwo.carparkfinderapp.data.remote.AvailabilityApi
import com.teamtwo.carparkfinderapp.domain.model.Availability
import com.teamtwo.carparkfinderapp.domain.repository.AvailabilityRepository
import com.teamtwo.carparkfinderapp.util.Resource
import retrofit2.HttpException
import java.io.IOException

class AvailabilityRepositoryImpl(
    private val api: AvailabilityApi,
    private val dao: AvailabilityDao
) : AvailabilityRepository {

    override suspend fun getAvailability(cparkNo: String): Resource<List<Availability>> {
        val response = try {
            api.getAvailabilityData().toResponseList()
        } catch (e: HttpException) {
            return Resource.Error("Something went wrong.")
        } catch (e: IOException) {
            return Resource.Error("Couldn't reach server, check your internet connection.")
        }
        dao.insertAvailabilities(response)
        val responseList = dao.getAvailability(cparkNo).map { it.toAvailability() }
        println(responseList)
        return Resource.Success(responseList)
    }

}