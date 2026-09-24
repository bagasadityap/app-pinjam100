package com.bagas.pinjam100.data.disbursement.remote

import com.bagas.pinjam100.data.rekening.remote.RekeningResponse
import kotlinx.serialization.Serializable

@Serializable
data class DisbursementResponse(
    val id: String,
    val loanApplicationId: String,
    val disbursementAmount: Double,
    val createdDate: String,
    val rekening: RekeningResponse
)