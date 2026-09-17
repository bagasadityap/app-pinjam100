package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.data.document.remote.DocumentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface DocumentRepository {
    suspend fun save(
        file: MultipartBody.Part,
        type: RequestBody,
        customerId: RequestBody
    ): DocumentResponse

    suspend fun delete(id: String): DocumentResponse
}