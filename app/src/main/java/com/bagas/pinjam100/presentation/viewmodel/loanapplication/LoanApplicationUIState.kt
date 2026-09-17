package com.bagas.pinjam100.presentation.viewmodel.loanapplication

import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication

data class LoanApplicationUIState(
    val loanApplications: List<LoanApplication> = emptyList(),
    val selectedLoanApplication: LoanApplication? = null,
    val createdLoanApplication: LoanApplication? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
)