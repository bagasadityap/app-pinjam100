package com.bagas.pinjam100.data.auth.remote

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String
) {

}