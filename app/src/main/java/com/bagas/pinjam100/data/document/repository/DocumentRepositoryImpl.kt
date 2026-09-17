package com.bagas.pinjam100.data.document.repository

import com.bagas.pinjam100.data.document.remote.DocumentApi
import com.bagas.pinjam100.data.document.remote.DocumentResponse
import com.bagas.pinjam100.domain.repository.DocumentRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val documentApi: DocumentApi
) : DocumentRepository {

    override suspend fun save(
        file: MultipartBody.Part,
        type: RequestBody,
        customerId: RequestBody
    ): DocumentResponse {
        return documentApi.save(
            file = file,
            type = type,
            customerId = customerId
        )
    }

    override suspend fun delete(id: String): DocumentResponse {
        return documentApi.delete(id)
    }
}