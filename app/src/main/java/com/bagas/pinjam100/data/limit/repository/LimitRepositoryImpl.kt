package com.bagas.pinjam100.data.limit.repository

import com.bagas.pinjam100.data.limit.remote.LimitApi
import com.bagas.pinjam100.data.limit.remote.toDomain
import com.bagas.pinjam100.domain.model.limit.Limit
import com.bagas.pinjam100.domain.repository.LimitRepository
import retrofit2.HttpException
import javax.inject.Inject

class LimitRepositoryImpl @Inject constructor(
    private val api: LimitApi
) : LimitRepository {

    override suspend fun getCustomerLimit(
        customerId: String
    ): Limit? {
        return try {
            api.getCustomerLimit(customerId).toDomain()
        } catch (e: HttpException) {
            if (e.code() == 404) {
                null
            } else {
                throw e
            }
        }
    }
}