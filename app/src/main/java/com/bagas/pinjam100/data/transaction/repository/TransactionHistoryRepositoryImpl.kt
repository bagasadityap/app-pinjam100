package com.bagas.pinjam100.data.transaction.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.map
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.core.error.runApiCatching
import com.bagas.pinjam100.data.transaction.remote.TransactionHistoryApi
import com.bagas.pinjam100.data.transaction.remote.toDomain
import com.bagas.pinjam100.domain.model.transaction.TransactionHistory
import com.bagas.pinjam100.domain.repository.TransactionHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class TransactionHistoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionHistoryApi,
    private val json: Json,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TransactionHistoryRepository {

    override suspend fun getByCustomerId(
        customerId: String
    ): AppResult<List<TransactionHistory>> =
        withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource
                    .getByCustomerId(customerId)
                    .requirePayload()
                    .map { response ->
                        response.map { it.toDomain() }
                    }
            }
        }
}