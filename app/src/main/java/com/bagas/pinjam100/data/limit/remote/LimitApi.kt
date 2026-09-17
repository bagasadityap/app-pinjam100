package com.bagas.pinjam100.data.limit.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface LimitApi {

    @GET("api/customer-limit/{id}/customer")
    suspend fun getCustomerLimit(
        @Path("id") customerId: String
    ): LimitResponse
}