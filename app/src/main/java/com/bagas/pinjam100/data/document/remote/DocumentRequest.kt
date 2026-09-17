package com.bagas.pinjam100.data.document.remote

import kotlinx.serialization.Serializable

@Serializable
data class DocumentRequest(
    val customerId: String,
    val type: String
)