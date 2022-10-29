package com.teamtwo.carparkfinderapp.data.remote.dto

import com.teamtwo.carparkfinderapp.data.local.entity.CarparkEntity
import com.teamtwo.carparkfinderapp.domain.model.Wrapper


data class WrapperDto(
    val help: String,
    val result: ResultDto,
    val success: Boolean
) {

    // we are only interested in the list of carparks in ResultDto,
    // so map them into a list of carpark entities

    fun toCarparkList(): List<CarparkEntity> {
        val wrapperObj = Wrapper(
            help = help,
            result = result,
            success = success
        )
        return wrapperObj.result.records.map { it.toCarparkEntity()}
    }
}