package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.domain.model.wilayah.Wilayah

interface WilayahRepository {

    suspend fun getProvinces(): Result<List<Wilayah>>

    suspend fun getRegencies(
        provinceCode: String
    ): Result<List<Wilayah>>

    suspend fun getDistricts(
        regencyCode: String
    ): Result<List<Wilayah>>

    suspend fun getVillages(
        districtCode: String
    ): Result<List<Wilayah>>
}