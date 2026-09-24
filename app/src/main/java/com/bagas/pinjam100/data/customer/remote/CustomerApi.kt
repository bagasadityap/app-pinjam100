package com.bagas.pinjam100.data.customer.remote

import com.bagas.pinjam100.core.error.ApiEnvelope
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerApi {
    @GET("api/customer/{id}/detail")
    suspend fun getDetailById(
        @Path("id") id: String
    ): ApiEnvelope<CustomerDetailResponse>

    @POST("api/customer/{id}/onboarding")
    suspend fun saveOnboarding(
        @Path("id") id: String,
        @Body request: CustomerOnboardingRequest
    ): ApiEnvelope<CustomerResponse>

    @PUT("api/customer/{id}/onboarding")
    suspend fun updateOnboarding(
        @Path("id") id: String,
        @Body request: CustomerOnboardingRequest
    ): ApiEnvelope<CustomerResponse>

    @PUT("api/customer/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Body request: CustomerRequest
    ): ApiEnvelope<CustomerResponse>

    @DELETE("api/customer/{id}")
    suspend fun delete(
        @Path("id") id: String
    ): ApiEnvelope<CustomerResponse>
}