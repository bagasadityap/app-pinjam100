package com.bagas.pinjam100.presentation.viewmodel.disbursement

import com.bagas.pinjam100.domain.model.disbursement.Disbursement

data class DisbursementUIState(
    val disbursement: Disbursement? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)