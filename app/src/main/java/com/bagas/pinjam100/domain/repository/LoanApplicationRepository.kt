package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication

interface LoanApplicationRepository {

    suspend fun getById(
        id: String
    ): LoanApplication

    suspend fun getByCustomer(
        customerId: String
    ): List<LoanApplication>

    suspend fun create(
        customerId: String,
        loanAmount: Long,
        tenorMonths: Int,
        purpose: String
    ): LoanApplication
}