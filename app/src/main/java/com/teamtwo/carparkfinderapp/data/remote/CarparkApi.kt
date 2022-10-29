package com.teamtwo.carparkfinderapp.data.remote

import com.teamtwo.carparkfinderapp.data.remote.dto.WrapperDto
import retrofit2.http.GET
import retrofit2.http.Query

// interface for api queries - CarparkRepository will do the implementation

interface CarparkApi {

    // retrieval from api will always be an async function (suspend)
    // GET format : base url(https://data.gov.sg/api/action/) + ""
    // base url is provided to the retrofit instance in AppModule

    // resource_id : 139a3035-e624-4f56-b63f-89ae28d4ae4c

    @GET("datastore_search")
    suspend fun getAllCarparks(
        @Query("resource_id") resource_id: String,
        @Query("limit") limit : Int
    ): WrapperDto
}
