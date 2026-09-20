package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.document.Document
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface DocumentRepository {

    suspend fun save(
        file: MultipartBody.Part,
        type: RequestBody,
        customerId: RequestBody
    ): AppResult<Document>

    suspend fun delete(id: String): AppResult<Document>
}