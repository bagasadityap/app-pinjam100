package com.bagas.pinjam100.data.limit.remote

import kotlinx.serialization.Serializable

@Serializable
data class LimitResponse(
    val id: String,
    val creditLimit: Double,
    val availableLimit: Double,
    val createdDate: String,
    val updatedDate: String
)