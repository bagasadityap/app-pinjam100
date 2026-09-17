package com.bagas.pinjam100.domain.usecase.loanapplication

import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication
import com.bagas.pinjam100.domain.repository.LoanApplicationRepository
import javax.inject.Inject

class GetCustomerLoanApplicationsUseCase @Inject constructor(
    private val repository: LoanApplicationRepository
) {

    suspend operator fun invoke(
        customerId: String
    ): List<LoanApplication> {
        return repository.getByCustomer(customerId)
    }
}