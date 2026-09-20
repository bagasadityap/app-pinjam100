package com.bagas.pinjam100.data.installment.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.map
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.core.error.runApiCatching
import com.bagas.pinjam100.data.installment.mapper.toDomain
import com.bagas.pinjam100.data.installment.remote.LoanInstallmentApi
import com.bagas.pinjam100.domain.model.installment.LoanInstallment
import com.bagas.pinjam100.domain.repository.LoanInstallmentRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class LoanInstallmentRepositoryImpl @Inject constructor(
    private val api: LoanInstallmentApi,
    private val json: Json
) : LoanInstallmentRepository {

    override suspend fun getById(
        id: String
    ): AppResult<LoanInstallment> {
        return runApiCatching(json) {
            api.getById(id)
                .requirePayload()
                .map { it.toDomain() }
        }
    }

    override suspend fun getByLoanApplicationId(
        loanApplicationId: String
    ): AppResult<List<LoanInstallment>> {
        return runApiCatching(json) {
            api.getByLoanApplicationId(loanApplicationId)
                .requirePayload()
                .map { response ->
                    response.map { it.toDomain() }
                }
        }
    }

    override suspend fun getByCustomerId(
        customerId: String
    ): AppResult<List<LoanInstallment>> {
        return runApiCatching(json) {
            api.getByCustomerId(customerId)
                .requirePayload()
                .map { response ->
                    response.map { it.toDomain() }
                }
        }
    }

    override suspend fun pay(
        id: String
    ): AppResult<LoanInstallment> {
        return runApiCatching(json) {
            api.pay(id)
                .requirePayload()
                .map { it.toDomain() }
        }
    }
}