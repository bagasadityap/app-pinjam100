package com.bagas.pinjam100.domain.usecase.loanapplication

import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication
import com.bagas.pinjam100.domain.repository.LoanApplicationRepository
import javax.inject.Inject

class GetLoanApplicationByIdUseCase @Inject constructor(
    private val loanApplicationRepository: LoanApplicationRepository
) {
    suspend operator fun invoke(
        id: String
    ): LoanApplication {
        return loanApplicationRepository.getById(id)
    }
}