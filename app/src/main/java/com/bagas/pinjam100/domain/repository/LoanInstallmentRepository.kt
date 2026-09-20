package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.installment.LoanInstallment

interface LoanInstallmentRepository {

    suspend fun getById(
        id: String
    ): AppResult<LoanInstallment>

    suspend fun getByLoanApplicationId(
        loanApplicationId: String
    ): AppResult<List<LoanInstallment>>

    suspend fun getByCustomerId(
        customerId: String
    ): AppResult<List<LoanInstallment>>

    suspend fun pay(
        id: String
    ): AppResult<LoanInstallment>
}