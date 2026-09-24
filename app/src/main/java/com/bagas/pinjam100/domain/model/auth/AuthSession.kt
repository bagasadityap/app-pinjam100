package com.bagas.pinjam100.domain.model.auth

data class AuthSession(
    val user: AuthUser? = null,
    val accessToken: String,
    val refreshToken: String,
    val expiresAtMillis: Long
) {
    fun isExpiredAt(nowMillis: Long): Boolean {
        return nowMillis >= expiresAtMillis
    }
}