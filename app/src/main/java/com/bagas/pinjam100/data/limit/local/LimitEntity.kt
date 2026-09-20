package com.bagas.pinjam100.data.limit.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "limits")
data class LimitEntity(
    @PrimaryKey
    val customerId: String,
    val id: String,
    val creditLimit: Double,
    val availableLimit: Double,
    val createdDate: String,
    val updatedDate: String
)