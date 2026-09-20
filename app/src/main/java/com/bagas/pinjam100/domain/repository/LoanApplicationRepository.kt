package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication

interface LoanApplicationRepository {

    suspend fun getById(
        id: String
    ): AppResult<LoanApplication>

    suspend fun getByCustomer(
        customerId: String
    ): AppResult<List<LoanApplication>>

    suspend fun create(
        customerId: String,
        loanAmount: Long,
        tenorMonths: Int,
        purpose: String
    ): AppResult<LoanApplication>
}