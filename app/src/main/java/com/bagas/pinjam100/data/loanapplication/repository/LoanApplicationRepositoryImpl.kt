package com.bagas.pinjam100.data.loanapplication.repository

import com.bagas.pinjam100.data.loanapplication.remote.LoanApplicationApi
import com.bagas.pinjam100.data.loanapplication.remote.LoanApplicationRequest
import com.bagas.pinjam100.data.loanapplication.remote.LoanApplicationResponse
import com.bagas.pinjam100.data.loanapplication.remote.toDomain
import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication
import com.bagas.pinjam100.domain.repository.LoanApplicationRepository
import javax.inject.Inject

class LoanApplicationRepositoryImpl @Inject constructor(
    private val api: LoanApplicationApi
) : LoanApplicationRepository {

    override suspend fun getById(
        id: String
    ): LoanApplication {
        return api.getById(id).toDomain()
    }

    override suspend fun getByCustomer(
        customerId: String
    ): List<LoanApplication> {
        return api.getByCustomer(customerId)
            .map { it.toDomain() }
    }

    override suspend fun create(
        customerId: String,
        loanAmount: Long,
        tenorMonths: Int,
        purpose: String
    ): LoanApplication {
        return api.create(
            LoanApplicationRequest(
                customerId = customerId,
                loanAmount = loanAmount.toString(),
                tenorMonths = tenorMonths,
                purpose = purpose
            )
        ).toDomain()
    }
}