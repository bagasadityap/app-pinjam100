package com.bagas.pinjam100.core.network

sealed interface CommonFailure : AppFailure {

    data class Network(val cause: Throwable? = null) : CommonFailure

    data class Unauthorized(
        val message: String?
    ) : CommonFailure

    data class ApiError(
        val code: String? = null,
        val details: List<String> = emptyList(),
        val message: String? = null,
    ) : CommonFailure

    data class Unexpected(val cause: Throwable? = null) : CommonFailure
}