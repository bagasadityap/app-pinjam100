package com.bagas.pinjam100.core.network

interface AppFailure {
    fun toMessage(): String? =
        when (this) {
            is CommonFailure.ApiError -> {
                details.joinToString(", ").ifBlank {
                    message ?: "Terjadi kesalahan pada API"
                }
            }

            is CommonFailure.Unauthorized ->
                message ?: "Akses tidak diizinkan."

            is CommonFailure.Network ->
                "Koneksi internet bermasalah."

            is CommonFailure.Unexpected ->
                "Terjadi kesalahan tidak terduga."

            else ->
                "Login gagal."
        }
}