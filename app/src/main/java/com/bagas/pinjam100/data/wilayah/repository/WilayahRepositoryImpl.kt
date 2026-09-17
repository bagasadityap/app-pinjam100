package com.bagas.pinjam100.data.wilayah.repository

import com.bagas.pinjam100.data.wilayah.remote.WilayahApi
import com.bagas.pinjam100.data.wilayah.remote.toDomain
import com.bagas.pinjam100.domain.model.wilayah.Wilayah
import com.bagas.pinjam100.domain.repository.WilayahRepository
import javax.inject.Inject

class WilayahRepositoryImpl @Inject constructor(
    private val api: WilayahApi
) : WilayahRepository {

    override suspend fun getProvinces(): Result<List<Wilayah>> {
        return runCatching {
            api.getProvinces().data.map { it.toDomain() }
        }
    }

    override suspend fun getRegencies(
        provinceCode: String
    ): Result<List<Wilayah>> {
        return runCatching {
            api.getRegencies(provinceCode).data.map { it.toDomain() }
        }
    }

    override suspend fun getDistricts(
        regencyCode: String
    ): Result<List<Wilayah>> {
        return runCatching {
            api.getDistricts(regencyCode).data.map { it.toDomain() }
        }
    }

    override suspend fun getVillages(
        districtCode: String
    ): Result<List<Wilayah>> {
        return runCatching {
            api.getVillages(districtCode).data.map { it.toDomain() }
        }
    }
}