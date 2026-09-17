package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.domain.model.installment.LoanInstallment

interface LoanInstallmentRepository {

    suspend fun getById(
        id: String
    ): LoanInstallment

    suspend fun getByLoanApplicationId(
        loanApplicationId: String
    ): List<LoanInstallment>

    suspend fun getByCustomerId(
        customerId: String
    ): List<LoanInstallment>

    suspend fun pay(
        id: String
    ): LoanInstallment
}