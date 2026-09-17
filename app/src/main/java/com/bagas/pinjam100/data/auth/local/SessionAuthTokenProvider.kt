package com.bagas.pinjam100.data.auth.local

import com.bagas.pinjam100.core.network.AuthTokenProvider
import kotlinx.coroutines.flow.first

class SessionAuthTokenProvider(
    private val localDataSource: AuthSessionLocalDataSource,
) : AuthTokenProvider {

    override suspend fun currentToken(): String? =
        localDataSource.observe().first()?.accessToken
}