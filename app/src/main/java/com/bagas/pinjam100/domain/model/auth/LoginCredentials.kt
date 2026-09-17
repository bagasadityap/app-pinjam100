package com.bagas.pinjam100.domain.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(
    val phoneNumber: String,
    val password: String
) {
    companion object {
    }
}