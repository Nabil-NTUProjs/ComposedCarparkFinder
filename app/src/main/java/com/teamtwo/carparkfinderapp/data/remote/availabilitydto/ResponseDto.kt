package com.teamtwo.carparkfinderapp.data.remote.availabilitydto

import com.teamtwo.carparkfinderapp.data.local.entity.AvailabilityEntity

data class ResponseDto(
    val items: List<ItemDto>
) {
    // map the responses to a list of Availability entities

    fun toResponseList(): List<AvailabilityEntity> {

        val responseObj = ResponseDto(items).items

        val responseList : List<AvailabilityEntity> = responseObj
            .flatMap { it.carpark_data }
            .map {
            AvailabilityEntity(
                carparkNumber = it.carpark_number,
                updateDateTime = it.update_datetime,
                lotType = it.carpark_info.map { it.lot_type }.toString(),
                totalLots = it.carpark_info.map { it.total_lots }.get(index = 0).toInt(),
                availableLots = it.carpark_info.map { it.lots_available }.get(index = 0).toInt()
            )
        }
        return responseList
    }
}