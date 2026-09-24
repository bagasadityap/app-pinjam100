package com.bagas.pinjam100.data.disbursement.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.map
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.core.error.runApiCatching
import com.bagas.pinjam100.data.disbursement.remote.DisbursementApi
import com.bagas.pinjam100.data.disbursement.remote.toDomain
import com.bagas.pinjam100.domain.model.disbursement.Disbursement
import com.bagas.pinjam100.domain.repository.DisbursementRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DisbursementRepositoryImpl @Inject constructor(
    private val api: DisbursementApi,
    private val json: Json
) : DisbursementRepository {

    override suspend fun getById(
        id: String
    ): AppResult<Disbursement> {
        return runApiCatching(json) {
            api.getById(id)
                .requirePayload()
                .map { it.toDomain() }
        }
    }
}