package com.bagas.pinjam100.data.loanapplication.remote

import com.bagas.pinjam100.data.customer.remote.CustomerResponse
import kotlinx.serialization.Serializable

@Serializable
data class LoanApplicationResponse(
    val id: String,
    val applicationId: String,
    val customer: CustomerResponse,
    val loanAmount: Double,
    val tenorMonths: Int,
    val interestRate: Double,
    val purpose: String?,
    val status: String,
    val createdDate: String
)