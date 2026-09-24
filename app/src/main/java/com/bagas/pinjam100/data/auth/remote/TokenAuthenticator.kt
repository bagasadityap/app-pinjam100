package com.bagas.pinjam100.core.network

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.data.auth.local.AuthSessionLocalDataSource
import com.bagas.pinjam100.data.auth.remote.AuthApi
import com.bagas.pinjam100.data.auth.remote.RefreshTokenRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Named

class TokenAuthenticator @Inject constructor(
    private val localDataSource: AuthSessionLocalDataSource,
    @Named("RefreshAuthApi") private val refreshAuthApi: AuthApi // Wajib tambahkan @Named("RefreshAuthApi") di sini
) : Authenticator {

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        // Cegah perulangan infinite retry jika token tetap 401
        if (responseCount(response) >= 2) {
            return null
        }

        return runBlocking {
            val session = localDataSource
                .observe()
                .firstOrNull()
                ?: return@runBlocking null

            if (session.refreshToken.isBlank()) {
                localDataSource.clear()
                return@runBlocking null
            }

            try {
                // Panggil refreshAuthApi tanpa interseptor
                val refreshResponse = refreshAuthApi.refreshToken(
                    RefreshTokenRequest(
                        refreshToken = session.refreshToken
                    )
                )

                val result = refreshResponse.requirePayload()

                if (result !is AppResult.Success) {
                    localDataSource.clear()
                    return@runBlocking null
                }

                val refreshData = result.data

                val newSession = session.copy(
                    accessToken = refreshData.token,
                    refreshToken = refreshData.refreshToken,
                    expiresAtMillis = refreshData.expiresAtMillis
                )

                localDataSource.save(newSession)

                // Return request baru dengan Access Token yang diperbarui
                response.request
                    .newBuilder()
                    .header(
                        "Authorization",
                        "Bearer ${newSession.accessToken}"
                    )
                    .build()

            } catch (e: Exception) {
                localDataSource.clear()
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var current = response.priorResponse

        while (current != null) {
            count++
            current = current.priorResponse
        }

        return count
    }
}