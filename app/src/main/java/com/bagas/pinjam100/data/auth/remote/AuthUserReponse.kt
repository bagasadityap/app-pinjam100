package com.bagas.pinjam100.data.auth.remote

import kotlinx.serialization.Serializable

@Serializable
data class AuthUserResponse(
    val id: String,
    val customerNumber: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val profileCompleted: Boolean
)