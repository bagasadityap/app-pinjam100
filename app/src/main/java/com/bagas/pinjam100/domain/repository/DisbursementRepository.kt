package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.disbursement.Disbursement

interface DisbursementRepository {

    suspend fun getById(
        id: String
    ): AppResult<Disbursement>
}