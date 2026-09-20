package com.bagas.pinjam100.data.transaction.remote

import com.bagas.pinjam100.core.error.ApiEnvelope
import retrofit2.http.GET
import retrofit2.http.Path

interface TransactionHistoryApi {

    @GET("api/transaction-history/{customerId}/customer")
    suspend fun getByCustomerId(
        @Path("customerId") customerId: String
    ): ApiEnvelope<List<TransactionHistoryResponse>>
}