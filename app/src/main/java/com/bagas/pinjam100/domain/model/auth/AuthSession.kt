package com.bagas.pinjam100.domain.model.auth

data class AuthSession(
    val user: AuthUser? = null,
    val accessToken: String,
    val expiresAtMillis: Long = 100_000L,
) {
    fun isExpiredAt(nowMillis: Long): Boolean = nowMillis >= expiresAtMillis
}