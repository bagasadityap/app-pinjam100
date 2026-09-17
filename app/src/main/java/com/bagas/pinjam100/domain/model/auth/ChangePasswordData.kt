package com.bagas.pinjam100.domain.model.auth

data class ChangePasswordData(
    val currentPassword: String,
    val newPassword: String,
)