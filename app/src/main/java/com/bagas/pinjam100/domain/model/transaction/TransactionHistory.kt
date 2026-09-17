package com.bagas.pinjam100.domain.model.transaction

import java.math.BigDecimal

data class TransactionHistory(
    val id: String,
    val type: TransactionType,
    val referenceNumber: String,
    val amount: BigDecimal,
    val date: String,
    val status: TransactionStatus
)

enum class TransactionType {
    DISBURSEMENT,
    INSTALLMENT_PAYMENT
}

enum class TransactionStatus {
    SUCCESS,
    PENDING,
    FAILED
}