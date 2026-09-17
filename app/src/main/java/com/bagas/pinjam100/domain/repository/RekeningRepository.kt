package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.rekening.Rekening

interface RekeningRepository {

    suspend fun getRekenings(): AppResult<List<Rekening>>

    suspend fun getRekening(
        id: String
    ): AppResult<Rekening>

    suspend fun addRekening(
        rekening: Rekening
    ): AppResult<Rekening>

    suspend fun updateRekening(
        rekening: Rekening
    ): AppResult<Rekening>

    suspend fun deleteRekening(
        id: String
    ): AppResult<Unit>
}