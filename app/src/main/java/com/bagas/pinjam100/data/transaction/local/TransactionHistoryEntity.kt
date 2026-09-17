package com.bagas.pinjam100.data.transaction.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_history")
data class TransactionHistoryEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val referenceNumber: String,
    val amount: Double,
    val date: String,
    val status: String
)