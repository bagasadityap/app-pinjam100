package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.auth.AuthSession
import com.bagas.pinjam100.domain.model.auth.ChangePasswordData
import com.bagas.pinjam100.domain.model.auth.ForgotPasswordData
import com.bagas.pinjam100.domain.model.auth.LoginCredentials
import com.bagas.pinjam100.domain.model.auth.RegisterData
import com.bagas.pinjam100.domain.model.auth.ResendOtpData
import com.bagas.pinjam100.domain.model.auth.ResetPasswordData
import com.bagas.pinjam100.domain.model.auth.VerifyOtpData
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(
        credentials: LoginCredentials
    ): AppResult<AuthSession>

    suspend fun refreshToken(
        refreshToken: String
    ): AppResult<AuthSession>

    suspend fun register(
        data: RegisterData
    ): AppResult<Unit>

    suspend fun verifyOtp(
        data: VerifyOtpData
    ): AppResult<AuthSession>

    suspend fun resendOtp(
        data: ResendOtpData
    ): AppResult<Unit>

    suspend fun forgotPassword(
        data: ForgotPasswordData
    ): AppResult<Unit>

    suspend fun resetPassword(
        data: ResetPasswordData
    ): AppResult<Unit>

    suspend fun changePassword(
        data: ChangePasswordData
    ): AppResult<Unit>

    suspend fun logout(): AppResult<Unit>

    suspend fun deleteAccount(): AppResult<Unit>

    fun observeSession(): Flow<AuthSession?>
}