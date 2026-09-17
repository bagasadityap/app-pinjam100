package com.bagas.pinjam100.data.loanapplication.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loan_application")
data class LoanApplicationEntity(
    @PrimaryKey
    val id: String,
    val applicationId: String,
    val customerId: String,
    val loanAmount: Long,
    val tenorMonths: Int,
    val interestRate: Double,
    val purpose: String?,
    val status: String
)