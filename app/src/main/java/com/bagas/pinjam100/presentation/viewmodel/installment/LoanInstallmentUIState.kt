package com.bagas.pinjam100.presentation.viewmodel.installment

import com.bagas.pinjam100.domain.model.installment.LoanInstallment

data class LoanInstallmentUIState(
    val installments: List<LoanInstallment> = emptyList(),
    val selectedInstallment: LoanInstallment? = null,
    val isLoading: Boolean = false,
    val isPaying: Boolean = false,
    val errorMessage: String? = null,
    val paymentSuccess: Boolean = false
)