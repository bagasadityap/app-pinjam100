package com.bagas.pinjam100.data.document.repository

import android.content.Context
import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.map
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.core.error.runApiCatching
import com.bagas.pinjam100.core.util.ImageCompressor
import com.bagas.pinjam100.data.document.remote.DocumentApi
import com.bagas.pinjam100.data.document.remote.toDomain
import com.bagas.pinjam100.domain.model.document.Document
import com.bagas.pinjam100.domain.repository.DocumentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.Buffer
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val documentApi: DocumentApi,
    @ApplicationContext private val context: Context,
    private val json: Json
) : DocumentRepository {

    override suspend fun save(
        file: MultipartBody.Part,
        type: RequestBody,
        customerId: RequestBody
    ): AppResult<Document> {
        return runApiCatching(json) {
            val tempFile = File(
                context.cacheDir,
                "temp_${System.currentTimeMillis()}.jpg"
            ).apply {
                FileOutputStream(this).use { output ->
                    val buffer = Buffer()
                    file.body.writeTo(buffer)
                    output.write(buffer.readByteArray())
                }
            }

            val compressedFile = ImageCompressor.compress(
                context = context,
                imageFile = tempFile,
                maxDimension = 1280,
                maxFileSizeKb = 500
            )

            tempFile.delete()

            val requestFile = compressedFile.asRequestBody(
                "image/jpeg".toMediaTypeOrNull()
            )

            val compressedPart = MultipartBody.Part.createFormData(
                "file",
                compressedFile.name,
                requestFile
            )

            documentApi.save(
                file = compressedPart,
                type = type,
                customerId = customerId
            )
                .requirePayload()
                .map { it.toDomain() }
        }
    }

    override suspend fun delete(id: String): AppResult<Document> {
        return runApiCatching(json) {
            documentApi.delete(id)
                .requirePayload()
                .map { it.toDomain() }
        }
    }
}