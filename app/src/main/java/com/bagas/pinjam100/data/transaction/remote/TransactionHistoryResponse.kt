package com.bagas.pinjam100.data.transaction.remote

import kotlinx.serialization.Serializable

@Serializable
data class TransactionHistoryResponse(
    val id: String,
    val type: String,
    val referenceNumber: String,
    val amount: Double,
    val date: String,
    val status: String
)