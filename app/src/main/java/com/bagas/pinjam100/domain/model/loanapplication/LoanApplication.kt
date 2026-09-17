package com.bagas.pinjam100.domain.model.loanapplication

import com.bagas.pinjam100.domain.model.customer.Customer
import java.math.BigDecimal

data class LoanApplication(
    val id: String,
    val applicationId: String,
    val customer: Customer,
    val loanAmount: BigDecimal,
    val tenorMonths: Int,
    val interestRate: BigDecimal,
    val purpose: String?,
    val status: String,
    val createdDate: String
)