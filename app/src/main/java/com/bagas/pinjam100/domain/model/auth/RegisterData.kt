package com.bagas.pinjam100.domain.model.auth

data class RegisterData(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)