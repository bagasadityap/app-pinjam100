package com.bagas.pinjam100.data.rekening.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rekening")
data class RekeningEntity(
    @PrimaryKey
    val id: String,
    val customerId: String,
    val namaBank: String,
    val noRekening: String
)