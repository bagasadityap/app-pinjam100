package com.bagas.pinjam100.data.document.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document")
data class DocumentEntity(
    @PrimaryKey
    val id: String,
    val customerId: String,
    val type: String,
    val fileUrl: String?,
    val verificationStatus: String,
    val verifiedBy: String?
)