package com.bagas.pinjam100.data.limit.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.map
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.core.error.runApiCatching
import com.bagas.pinjam100.data.limit.local.LimitDao
import com.bagas.pinjam100.data.limit.mapper.toDomain
import com.bagas.pinjam100.data.limit.mapper.toEntity
import com.bagas.pinjam100.data.limit.remote.LimitApi
import com.bagas.pinjam100.domain.model.limit.Limit
import com.bagas.pinjam100.domain.repository.LimitRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class LimitRepositoryImpl @Inject constructor(
    private val api: LimitApi,
    private val dao: LimitDao,
    private val json: Json
) : LimitRepository {

    override suspend fun getCustomerLimit(
        customerId: String
    ): AppResult<Limit?> {

        val result = runApiCatching(json) {
            api.getCustomerLimit(customerId)
                .requirePayload()
                .map { response ->
                    response.toEntity() to response.toDomain()
                }
        }

        return when (result) {

            is AppResult.Success -> {
                val (entity, domain) = result.data

                dao.upsert(entity)

                AppResult.Success(domain)
            }

            is AppResult.Failure -> {
                dao.getByCustomerId(customerId)
                    ?.toDomain()
                    ?.let { localLimit ->
                        AppResult.Success(localLimit)
                    }
                    ?: result
            }
        }
    }
}