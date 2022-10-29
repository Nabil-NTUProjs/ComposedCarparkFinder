package com.teamtwo.carparkfinderapp.data.remote.dto

data class ResultDto(
    val _links: LinksDto,
    val fields: List<FieldDto>,
    val records: List<CarparkDto>,
    val resource_id: String,
    val total: Int
)