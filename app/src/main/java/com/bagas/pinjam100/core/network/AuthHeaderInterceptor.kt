package com.bagas.pinjam100.core.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_AUTHORIZATION = "Authorization"

class AuthHeaderInterceptor(
    private val tokenProvider: AuthTokenProvider,
) : Interceptor {

    private val publicPaths = setOf(
        "/auth/login",
        "/auth/register",
        "/auth/verify-otp",
        "/auth/resend-otp"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.header(HEADER_AUTHORIZATION) != null) {
            return chain.proceed(request)
        }

        if (request.url.encodedPath in publicPaths) {
            return chain.proceed(request)
        }

        val token = runBlocking { tokenProvider.currentToken() }
            ?: return chain.proceed(request)

        return chain.proceed(
            request.newBuilder()
                .header(HEADER_AUTHORIZATION, "Bearer $token")
                .build()
        )
    }
}