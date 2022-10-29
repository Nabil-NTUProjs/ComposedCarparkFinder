package com.teamtwo.carparkfinderapp.data.repository

import com.teamtwo.carparkfinderapp.data.local.CarparkDao
import com.teamtwo.carparkfinderapp.data.remote.CarparkApi
import com.teamtwo.carparkfinderapp.domain.model.Carpark
import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository
import com.teamtwo.carparkfinderapp.util.Constants
import com.teamtwo.carparkfinderapp.util.Resource
import retrofit2.HttpException
import java.io.IOException

class CarparkRepositoryImpl(
    private val api: CarparkApi,
    private val dao: CarparkDao
) : CarparkRepository {

    override suspend fun getCarparks(): Resource<List<Carpark>> {
        val response = try {
            api.getAllCarparks(Constants.RESOURCE_ID, Constants.LIMIT).toCarparkList()
        } catch (e: HttpException) {
            return Resource.Error("Something went wrong.")
        } catch (e: IOException) {
            return Resource.Error("Couldn't reach server, check your internet connection.")
        }
        dao.insertCarparks(response)
        val responseList = dao.getCarparks().map { it.toCarpark() }
        println(responseList)
        return Resource.Success(responseList)
    }

    override suspend fun getBookmarkedCarparks(): Resource<List<Carpark>> {
        val response = try {
            api.getAllCarparks(Constants.RESOURCE_ID, Constants.LIMIT).toCarparkList()
        } catch (e: HttpException) {
            return Resource.Error("Something went wrong.")
        } catch (e: IOException) {
            return Resource.Error("Couldn't reach server, check your internet connection.")
        }
        dao.insertCarparks(response)
        val responseList = dao.getBookmarkedCarparks().map { it.toCarpark() }
        println(responseList)
        return Resource.Success(responseList)
    }

    // makes the call to the dao to update the bookmarked value based on the provided carpark id
    override suspend fun updateBookmark(flag: Int, id: Int): Boolean {
        dao.updateBookmark(flag, id)
        return true
    }
}