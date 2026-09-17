package com.bagas.pinjam100.domain.model.installment

import java.math.BigDecimal

data class LoanInstallment(
    val id: String,
    val loanApplicationId: String,
    val installmentNumber: String,
    val installmentSequence: Int,
    val dueDate: String,
    val installmentAmount: BigDecimal,
    val paidAmount: BigDecimal,
    val status: InstallmentStatus,
    val paidDate: String?,
    val createdDate: String,
    val updatedDate: String?
)

enum class InstallmentStatus {
    PAID,
    UNPAID
}