package com.bagas.pinjam100.data.rekening.remote

import kotlinx.serialization.Serializable

@Serializable
data class RekeningRequest(
    val namaBank: String,
    val noRekening: String,
    val accountHolder: String
)