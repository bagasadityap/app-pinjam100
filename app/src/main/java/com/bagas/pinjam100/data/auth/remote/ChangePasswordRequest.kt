package com.bagas.pinjam100.data.auth.remote

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
)