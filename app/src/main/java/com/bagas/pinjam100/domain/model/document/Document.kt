package com.bagas.pinjam100.domain.model.document

data class Document(
    val id: String,
    val customerId: String,
    val fileUrl: String,
    val type: String,
    val verificationStatus: String,
    val verifiedBy: String?
)