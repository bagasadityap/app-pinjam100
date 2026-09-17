package com.bagas.pinjam100.data.wilayah.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WilayahResponse(
    val data: List<WilayahDto>,
    val meta: WilayahMetaDto
)

@Serializable
data class WilayahDto(
    val code: String,
    val name: String
)

@Serializable
data class WilayahMetaDto(
    @SerialName("administrative_area_level")
    val administrativeAreaLevel: Int,
    @SerialName("updated_at")
    val updatedAt: String
)