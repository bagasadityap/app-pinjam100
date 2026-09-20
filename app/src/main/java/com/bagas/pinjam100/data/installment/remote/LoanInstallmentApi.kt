package com.bagas.pinjam100.data.installment.remote

import com.bagas.pinjam100.core.error.ApiEnvelope
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LoanInstallmentApi {

    @GET("api/installment/{id}")
    suspend fun getById(
        @Path("id") id: String
    ): ApiEnvelope<LoanInstallmentResponse>

    @GET("api/installment/{loanApplicationId}/loan-application")
    suspend fun getByLoanApplicationId(
        @Path("loanApplicationId") loanApplicationId: String
    ): ApiEnvelope<List<LoanInstallmentResponse>>

    @GET("api/installment/{customerId}/customer")
    suspend fun getByCustomerId(
        @Path("customerId") customerId: String
    ): ApiEnvelope<List<LoanInstallmentResponse>>

    @POST("api/installment/{id}/pay")
    suspend fun pay(
        @Path("id") id: String
    ): ApiEnvelope<LoanInstallmentResponse>
}