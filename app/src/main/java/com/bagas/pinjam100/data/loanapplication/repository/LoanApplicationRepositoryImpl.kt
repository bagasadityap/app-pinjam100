package com.bagas.pinjam100.data.loanapplication.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.map
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.core.error.runApiCatching
import com.bagas.pinjam100.data.loanapplication.remote.LoanApplicationApi
import com.bagas.pinjam100.data.loanapplication.remote.LoanApplicationRequest
import com.bagas.pinjam100.data.loanapplication.remote.toDomain
import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication
import com.bagas.pinjam100.domain.repository.LoanApplicationRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class LoanApplicationRepositoryImpl @Inject constructor(
    private val api: LoanApplicationApi,
    private val json: Json
) : LoanApplicationRepository {

    override suspend fun getById(
        id: String
    ): AppResult<LoanApplication> {
        return runApiCatching(json) {
            api.getById(id)
                .requirePayload()
                .map { it.toDomain() }
        }
    }

    override suspend fun getByCustomer(
        customerId: String
    ): AppResult<List<LoanApplication>> {
        return runApiCatching(json) {
            api.getByCustomer(customerId)
                .requirePayload()
                .map { response ->
                    response.map { it.toDomain() }
                }
        }
    }

    override suspend fun create(
        customerId: String,
        loanAmount: Long,
        tenorMonths: Int,
        purpose: String
    ): AppResult<LoanApplication> {
        return runApiCatching(json) {
            api.create(
                LoanApplicationRequest(
                    customerId = customerId,
                    loanAmount = loanAmount.toString(),
                    tenorMonths = tenorMonths,
                    purpose = purpose
                )
            )
                .requirePayload()
                .map { it.toDomain() }
        }
    }
}