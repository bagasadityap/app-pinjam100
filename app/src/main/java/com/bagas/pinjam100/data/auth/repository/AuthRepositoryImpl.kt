package com.bagas.pinjam100.data.auth.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.map
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.core.error.runApiCatching
import com.bagas.pinjam100.data.auth.local.AuthSessionLocalDataSource
import com.bagas.pinjam100.data.auth.remote.AuthApi
import com.bagas.pinjam100.data.auth.remote.LoginRequest
import com.bagas.pinjam100.data.auth.remote.RefreshTokenRequest
import com.bagas.pinjam100.data.auth.remote.VerifyOtpRequest
import com.bagas.pinjam100.data.auth.remote.toDomain
import com.bagas.pinjam100.data.auth.remote.toRequest
import com.bagas.pinjam100.domain.model.auth.AuthSession
import com.bagas.pinjam100.domain.model.auth.ChangePasswordData
import com.bagas.pinjam100.domain.model.auth.ForgotPasswordData
import com.bagas.pinjam100.domain.model.auth.LoginCredentials
import com.bagas.pinjam100.domain.model.auth.LogoutData
import com.bagas.pinjam100.domain.model.auth.RegisterData
import com.bagas.pinjam100.domain.model.auth.ResendOtpData
import com.bagas.pinjam100.domain.model.auth.ResetPasswordData
import com.bagas.pinjam100.domain.model.auth.VerifyOtpData
import com.bagas.pinjam100.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val localDataSource: AuthSessionLocalDataSource,
    private val remoteDataSource: AuthApi,
    private val json: Json,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: () -> Long = System::currentTimeMillis
) : AuthRepository {

    override suspend fun login(
        credentials: LoginCredentials
    ): AppResult<AuthSession> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.login(
                    LoginRequest(
                        phoneNumber = credentials.phoneNumber,
                        password = credentials.password,
                        fcmToken = credentials.fcmToken
                    )
                )
                    .requirePayload()
                    .map { response ->
                        response.toDomain()
                    }
                    .also { result ->
                        if (result is AppResult.Success) {
                            localDataSource.save(result.data)
                        }
                    }
            }
        }
    }

    override suspend fun register(
        data: RegisterData
    ): AppResult<Unit> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.register(
                    data.toRequest()
                )

                AppResult.success(Unit)
            }
        }
    }

    override suspend fun verifyOtp(
        data: VerifyOtpData
    ): AppResult<AuthSession> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.verifyOtp(
                    VerifyOtpRequest(
                        phoneNumber = data.phoneNumber,
                        otpCode = data.otpCode
                    )
                )
                    .requirePayload()
                    .map { response ->
                        response.toDomain()
                    }
                    .also { result ->
                        if (result is AppResult.Success) {
                            localDataSource.save(result.data)
                        }
                    }
            }
        }
    }

    override suspend fun resendOtp(
        data: ResendOtpData
    ): AppResult<Unit> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.resendOtp(
                    data.toRequest()
                )

                AppResult.success(Unit)
            }
        }
    }

    override suspend fun forgotPassword(
        data: ForgotPasswordData
    ): AppResult<Unit> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.forgotPassword(
                    data.toRequest()
                )

                AppResult.success(Unit)
            }
        }
    }

    override suspend fun resetPassword(
        data: ResetPasswordData
    ): AppResult<Unit> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.resetPassword(
                    data.toRequest()
                )

                AppResult.success(Unit)
            }
        }
    }

    override suspend fun changePassword(
        data: ChangePasswordData
    ): AppResult<Unit> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.changePassword(
                    data.toRequest()
                )

                AppResult.success(Unit)
            }
        }
    }

    override suspend fun logout(): AppResult<Unit> {
        return withContext(ioDispatcher) {
            val refreshToken = localDataSource.currentSession()?.refreshToken

            try {
                if (refreshToken.isNullOrBlank()) {
                    AppResult.success(Unit)
                } else {
                    runApiCatching(json) {
                        remoteDataSource.logout(
                            LogoutData(refreshToken = refreshToken).toRequest()
                        )
                        AppResult.success(Unit)
                    }
                }
            } finally {
                localDataSource.clear()
            }
        }
    }

    override suspend fun deleteAccount(): AppResult<Unit> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.deleteAccount()
                    .requirePayload()
                    .also {
                        localDataSource.clear()
                    }
            }
        }
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): AppResult<AuthSession> {
        return withContext(ioDispatcher) {
            runApiCatching(json) {
                remoteDataSource.refreshToken(
                    RefreshTokenRequest(
                        refreshToken = refreshToken
                    )
                )
                    .requirePayload()
                    .map { response ->
                        response.toDomain()
                    }
                    .also { result ->
                        if (result is AppResult.Success) {
                            localDataSource.save(result.data)
                        }
                    }
            }
        }
    }

    override fun observeSession(): Flow<AuthSession?> {
        return localDataSource
            .observe()
            .map { session ->
                session?.takeUnless {
                    it.isExpiredAt(clock())
                }
            }
    }
}