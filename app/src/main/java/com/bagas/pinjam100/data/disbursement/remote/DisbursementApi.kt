package com.bagas.pinjam100.data.disbursement.remote

import com.bagas.pinjam100.core.error.ApiEnvelope
import retrofit2.http.GET
import retrofit2.http.Path

interface DisbursementApi {

    @GET("api/disbursement/{id}")
    suspend fun getById(
        @Path("id") id: String
    ): ApiEnvelope<DisbursementResponse>
}