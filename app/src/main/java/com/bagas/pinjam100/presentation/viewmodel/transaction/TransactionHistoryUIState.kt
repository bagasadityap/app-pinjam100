package com.bagas.pinjam100.presentation.viewmodel.transaction

import com.bagas.pinjam100.domain.model.transaction.TransactionHistory

data class TransactionHistoryUIState(
    val transactions: List<TransactionHistory> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)