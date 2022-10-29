package com.teamtwo.carparkfinderapp.data.remote

import com.teamtwo.carparkfinderapp.data.remote.availabilitydto.ResponseDto
import retrofit2.http.GET

interface AvailabilityApi {

    @GET("carpark-availability")
    suspend fun getAvailabilityData(): ResponseDto
}