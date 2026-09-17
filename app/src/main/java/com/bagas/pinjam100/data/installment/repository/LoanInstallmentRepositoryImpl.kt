package com.bagas.pinjam100.data.installment.repository

import com.bagas.pinjam100.data.installment.mapper.toDomain
import com.bagas.pinjam100.data.installment.remote.LoanInstallmentApi
import com.bagas.pinjam100.domain.model.installment.LoanInstallment
import com.bagas.pinjam100.domain.repository.LoanInstallmentRepository
import javax.inject.Inject

class LoanInstallmentRepositoryImpl @Inject constructor(
    private val api: LoanInstallmentApi
) : LoanInstallmentRepository {

    override suspend fun getById(
        id: String
    ): LoanInstallment {
        return api.getById(id).toDomain()
    }

    override suspend fun getByLoanApplicationId(
        loanApplicationId: String
    ): List<LoanInstallment> {
        return api.getByLoanApplicationId(loanApplicationId)
            .map { it.toDomain() }
    }

    override suspend fun getByCustomerId(
        customerId: String
    ): List<LoanInstallment> {
        return api.getByCustomerId(customerId)
            .map { it.toDomain() }
    }

    override suspend fun pay(
        id: String
    ): LoanInstallment {
        return api.pay(id).toDomain()
    }
}