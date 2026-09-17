package com.bagas.pinjam100.data.installment.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loan_installment")
data class LoanInstallmentEntity(
    @PrimaryKey
    val id: String,
    val loanApplicationId: String,
    val installmentNumber: String,
    val installmentSequence: Int,
    val dueDate: String,
    val installmentAmount: Long,
    val paidAmount: Long,
    val status: String,
    val paidDate: String?,
    val createdDate: String,
    val updatedDate: String?
)