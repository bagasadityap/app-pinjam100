package com.bagas.pinjam100.data.installment.remote

import kotlinx.serialization.Serializable

@Serializable
data class LoanInstallmentResponse(
    val id: String,
    val loanApplicationId: String,
    val installmentNumber: String,
    val installmentSequence: Int,
    val dueDate: String,
    val installmentAmount: Double,
    val paidAmount: Double,
    val status: String,
    val paidDate: String?,
    val createdDate: String,
    val updatedDate: String?
)