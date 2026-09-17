package com.bagas.pinjam100.data.auth.remote

import com.bagas.pinjam100.domain.model.auth.AuthSession
import com.bagas.pinjam100.domain.model.auth.AuthUser
import com.bagas.pinjam100.domain.model.auth.ChangePasswordData
import com.bagas.pinjam100.domain.model.auth.ForgotPasswordData
import com.bagas.pinjam100.domain.model.auth.LoginCredentials
import com.bagas.pinjam100.domain.model.auth.RegisterData
import com.bagas.pinjam100.domain.model.auth.ResendOtpData
import com.bagas.pinjam100.domain.model.auth.ResetPasswordData
import com.bagas.pinjam100.domain.model.auth.VerifyOtpData

fun LoginCredentials.toRequest() = LoginRequest(
    phoneNumber = phoneNumber,
    password = password,
    fcmToken = fcmToken
)

fun LoginResponse.toDomain() = AuthSession(
    user = user?.toDomain(),
    accessToken = token,
    expiresAtMillis = expiresAtMillis
)

fun AuthUserResponse.toDomain() = AuthUser(
    id = id,
    customerNumber = customerNumber,
    fullName = fullName,
    phoneNumber = phoneNumber,
    email = email,
    profileCompleted = profileCompleted
)

fun RegisterData.toRequest() = RegisterRequest(
    fullName = fullName,
    email = email,
    phoneNumber = phoneNumber,
    password = password
)

fun VerifyOtpData.toRequest() = VerifyOtpRequest(
    phoneNumber = phoneNumber,
    otpCode = otpCode
)

fun ResendOtpData.toRequest() = ResendOtpRequest(
    phoneNumber = phoneNumber
)

fun ForgotPasswordData.toRequest() = ForgotPasswordRequest(
    email = email
)

fun ResetPasswordData.toRequest() = ResetPasswordRequest(
    token = token,
    password = password
)

fun ChangePasswordData.toRequest() = ChangePasswordRequest(
    currentPassword = currentPassword,
    newPassword = newPassword,
)