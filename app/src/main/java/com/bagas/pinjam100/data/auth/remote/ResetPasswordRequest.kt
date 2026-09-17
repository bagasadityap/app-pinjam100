package com.bagas.pinjam100.data.auth.remote

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val token: String,
    val password: String
)