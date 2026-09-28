package com.bagas.pinjam100.core.network

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.data.auth.local.AuthSessionLocalDataSource
import com.bagas.pinjam100.data.auth.remote.AuthApi
import com.bagas.pinjam100.data.auth.remote.RefreshTokenRequest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

class TokenAuthenticator @Inject constructor(
    private val localDataSource: AuthSessionLocalDataSource,
    @Named("RefreshAuthApi") private val refreshAuthApi: AuthApi,
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        if (responseCount(response) >= 2) {
            return null
        }

        return runBlocking {
            mutex.withLock {
                val session = localDataSource
                    .observe()
                    .firstOrNull()
                    ?: return@runBlocking null

                if (session.refreshToken.isBlank()) {
                    localDataSource.clear()
                    return@runBlocking null
                }

                // Jika token di request sudah berbeda dengan token di session saat ini,
                // berarti thread lain sudah berhasil melakukan refresh sebelumnya.
                val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")
                if ((!requestToken.isNullOrBlank()) && (requestToken != session.accessToken)) {
                    return@runBlocking response.request
                        .newBuilder()
                        .header("Authorization", "Bearer ${session.accessToken}")
                        .build()
                }

                try {
                    val refreshResponse = refreshAuthApi.refreshToken(
                        RefreshTokenRequest(
                            refreshToken = session.refreshToken,
                        ),
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
                        expiresAtMillis = refreshData.expiresAtMillis,
                    )

                    localDataSource.save(newSession)

                    response.request
                        .newBuilder()
                        .header(
                            "Authorization",
                            "Bearer ${newSession.accessToken}",
                        )
                        .build()

                } catch (e: HttpException) {
                    // Hanya hapus sesi jika server menolak refresh token dengan 401/403 (expired/invalid)
                    if ((e.code() == 401) || (e.code() == 403)) {
                        localDataSource.clear()
                    }
                    null
                } catch (_: Exception) {
                    // Jangan hapus sesi pada gangguan koneksi/timeout (IOException, dll)
                    null
                }
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
