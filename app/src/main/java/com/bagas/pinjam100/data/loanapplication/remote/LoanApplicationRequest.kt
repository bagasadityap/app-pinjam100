package com.bagas.pinjam100.data.loanapplication.remote

import kotlinx.serialization.Serializable

@Serializable
data class LoanApplicationRequest(
    val customerId: String,
    val branchId: String? = null,
    val loanAmount: String,
    val tenorMonths: Int,
    val purpose: String
)