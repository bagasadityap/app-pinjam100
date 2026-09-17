package com.bagas.pinjam100.data.document.remote

import kotlinx.serialization.Serializable

@Serializable
data class DocumentResponse(
    val id: String,
    val customerId: String,
    val fileUrl: String,
    val type: String,
    val verificationStatus: String,
    val verifiedBy: String?
)