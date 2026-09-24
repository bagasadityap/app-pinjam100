package com.bagas.pinjam100.domain.usecase.loanapplication

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication
import com.bagas.pinjam100.domain.repository.LoanApplicationRepository
import javax.inject.Inject

class CreateLoanApplicationUseCase @Inject constructor(
    private val repository: LoanApplicationRepository
) {

    suspend operator fun invoke(
        customerId: String,
        loanAmount: Long,
        tenorMonths: Int,
        purpose: String
    ): AppResult<LoanApplication> {
        return repository.create(
            customerId = customerId,
            loanAmount = loanAmount,
            tenorMonths = tenorMonths,
            purpose = purpose
        )
    }
}