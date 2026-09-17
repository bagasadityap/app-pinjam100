package com.bagas.pinjam100.data.auth.remote

import com.bagas.pinjam100.core.error.ApiEnvelope
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/customer/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiEnvelope<LoginResponse>

    @POST("api/auth/customer/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiEnvelope<Unit>

    @POST("api/auth/customer/verify-otp")
    suspend fun verifyOtp(
        @Body body: VerifyOtpRequest
    ): ApiEnvelope<LoginResponse>

    @POST("api/auth/customer/resend-otp")
    suspend fun resendOtp(
        @Body body: ResendOtpRequest
    ): ApiEnvelope<Unit>

    @POST("api/auth/customer/forgot-password")
    suspend fun forgotPassword(
        @Body body: ForgotPasswordRequest
    ): ApiEnvelope<Unit>

    @POST("api/auth/customer/reset-password")
    suspend fun resetPassword(
        @Body body: ResetPasswordRequest
    ): ApiEnvelope<Unit>

    @POST("api/auth/customer/change-password")
    suspend fun changePassword(
        @Body body: ChangePasswordRequest
    ): ApiEnvelope<Unit>

    @POST("api/auth/customer/logout")
    suspend fun logout()

    @DELETE("api/auth/customer/account")
    suspend fun deleteAccount(): ApiEnvelope<Unit>
}