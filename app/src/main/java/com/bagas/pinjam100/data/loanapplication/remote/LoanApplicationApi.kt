package com.bagas.pinjam100.data.loanapplication.remote

import com.bagas.pinjam100.core.error.ApiEnvelope
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LoanApplicationApi {

    @GET("api/loan-application/{id}")
    suspend fun getById(
        @Path("id") id: String
    ): ApiEnvelope<LoanApplicationResponse>

    @GET("api/loan-application/{id}/customer")
    suspend fun getByCustomer(
        @Path("id") customerId: String
    ): ApiEnvelope<List<LoanApplicationResponse>>

    @POST("api/loan-application")
    suspend fun create(
        @Body request: LoanApplicationRequest
    ): ApiEnvelope<LoanApplicationResponse>
}