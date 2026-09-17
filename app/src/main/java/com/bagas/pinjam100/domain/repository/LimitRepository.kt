package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.domain.model.limit.Limit

interface LimitRepository {

    suspend fun getCustomerLimit(
        customerId: String
    ): Limit?
}