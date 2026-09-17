package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.transaction.TransactionHistory

interface TransactionHistoryRepository {

    suspend fun getByCustomerId(
        customerId: String
    ): AppResult<List<TransactionHistory>>
}