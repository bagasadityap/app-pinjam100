package com.bagas.pinjam100.data.document.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface DocumentApi {

    @Multipart
    @POST("api/document")
    suspend fun save(
        @Part file: MultipartBody.Part,
        @Part("type") type: RequestBody,
        @Part("customerId") customerId: RequestBody
    ): DocumentResponse

    @DELETE("api/document/{id}")
    suspend fun delete(
        @Path("id") id: String
    ): DocumentResponse
}