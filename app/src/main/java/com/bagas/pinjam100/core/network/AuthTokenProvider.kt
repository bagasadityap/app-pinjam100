package com.bagas.pinjam100.core.network

fun interface AuthTokenProvider {
    suspend fun currentToken(): String?
}