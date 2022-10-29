package com.teamtwo.carparkfinderapp.domain.model

import com.teamtwo.carparkfinderapp.data.remote.dto.ResultDto

data class Wrapper(
    val help: String,
    val result: ResultDto,
    val success: Boolean
)
