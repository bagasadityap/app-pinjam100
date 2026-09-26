package com.bagas.pinjam100.domain.model.auth

data class AuthUser(
    val id: String,
    val customerNumber: String,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val profileCompleted: Boolean,
    val verificationStatus: String
)