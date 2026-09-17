package com.bagas.pinjam100.domain.model.auth

data class ResetPasswordData(
    val token: String,
    val password: String
)