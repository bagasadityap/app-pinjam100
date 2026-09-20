package com.bagas.pinjam100.data.auth.remote

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val nationalId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
)