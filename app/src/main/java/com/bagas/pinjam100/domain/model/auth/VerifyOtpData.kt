package com.bagas.pinjam100.domain.model.auth

data class VerifyOtpData(
    val phoneNumber: String,
    val otpCode: String
)