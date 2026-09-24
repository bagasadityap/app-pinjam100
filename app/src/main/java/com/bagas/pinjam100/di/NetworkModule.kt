package com.bagas.pinjam100.di

import android.content.Context
import com.bagas.pinjam100.BuildConfig
import com.bagas.pinjam100.core.network.AuthHeaderInterceptor
import com.bagas.pinjam100.core.network.AuthTokenProvider
import com.bagas.pinjam100.core.network.TokenAuthenticator
import com.bagas.pinjam100.data.auth.local.AuthSessionLocalDataSource
import com.bagas.pinjam100.data.auth.local.SessionAuthTokenProvider
import com.bagas.pinjam100.data.auth.remote.AuthApi
import com.bagas.pinjam100.data.customer.remote.CustomerApi
import com.bagas.pinjam100.data.disbursement.remote.DisbursementApi
import com.bagas.pinjam100.data.document.remote.DocumentApi
import com.bagas.pinjam100.data.installment.remote.LoanInstallmentApi
import com.bagas.pinjam100.data.limit.remote.LimitApi
import com.bagas.pinjam100.data.loanapplication.remote.LoanApplicationApi
import com.bagas.pinjam100.data.transaction.remote.TransactionHistoryApi
import com.bagas.pinjam100.data.wilayah.remote.WilayahApi
import com.bagas.pinjam100.data.wilayah.repository.WilayahRepositoryImpl
import com.bagas.pinjam100.domain.repository.WilayahRepository
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val TIMEOUT_SECONDS = 30L
private const val HEADER_AUTHORIZATION = "Authorization"
private const val JSON_MEDIA_TYPE = "application/json"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // ---------------------------------------------------------
    // JSON
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            coerceInputValues = true
        }

    // ---------------------------------------------------------
    // AUTH TOKEN PROVIDER
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideAuthTokenProvider(
        localDataSource: AuthSessionLocalDataSource
    ): AuthTokenProvider =
        SessionAuthTokenProvider(localDataSource)

    // ---------------------------------------------------------
    // REFRESH CLIENT
    // ---------------------------------------------------------
    //
    // Client ini TIDAK mempunyai:
    // - AuthHeaderInterceptor
    // - TokenAuthenticator
    //
    // Tujuannya agar request refresh tidak memicu refresh lagi.
    //

    @Provides
    @Singleton
    @Named("RefreshClient")
    fun provideRefreshOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
            .readTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
            .build()

    // ---------------------------------------------------------
    // REFRESH AUTH API
    // ---------------------------------------------------------
    //
    // Tetap menggunakan interface AuthApi.
    // Hanya Retrofit/client-nya yang berbeda.
    //

    @Provides
    @Singleton
    @Named("RefreshAuthApi")
    fun provideRefreshAuthApi(
        @Named("RefreshClient") client: OkHttpClient,
        json: Json
    ): AuthApi =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory(
                    JSON_MEDIA_TYPE.toMediaType()
                )
            )
            .build()
            .create(AuthApi::class.java)

    // ---------------------------------------------------------
    // TOKEN AUTHENTICATOR
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        localDataSource: AuthSessionLocalDataSource,
        @Named("RefreshAuthApi") refreshAuthApi: AuthApi
    ): TokenAuthenticator =
        TokenAuthenticator(
            localDataSource = localDataSource,
            refreshAuthApi = refreshAuthApi
        )

    // ---------------------------------------------------------
    // MAIN OKHTTP CLIENT
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        tokenProvider: AuthTokenProvider,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
            .readTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                TIMEOUT_SECONDS,
                TimeUnit.SECONDS
            )

            // Tambahkan access token ke request
            .addInterceptor(
                AuthHeaderInterceptor(tokenProvider)
            )

            // Jika 401 -> refresh token
            .authenticator(
                tokenAuthenticator
            )

            // Chucker
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .redactHeaders(HEADER_AUTHORIZATION)
                    .alwaysReadResponseBody(true)
                    .build()
            )

            .build()

    // ---------------------------------------------------------
    // MAIN RETROFIT
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory(
                    JSON_MEDIA_TYPE.toMediaType()
                )
            )
            .build()

    // ---------------------------------------------------------
    // AUTH API
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideAuthApi(
        retrofit: Retrofit
    ): AuthApi =
        retrofit.create(AuthApi::class.java)

    // ---------------------------------------------------------
    // WILAYAH
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideWilayahApi(
        retrofit: Retrofit
    ): WilayahApi =
        retrofit
            .newBuilder()
            .baseUrl("https://wilayah.id/api/")
            .build()
            .create(WilayahApi::class.java)

    @Provides
    @Singleton
    fun provideWilayahRepository(
        api: WilayahApi
    ): WilayahRepository =
        WilayahRepositoryImpl(api)

    // ---------------------------------------------------------
    // CUSTOMER
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideCustomerApi(
        retrofit: Retrofit
    ): CustomerApi =
        retrofit.create(CustomerApi::class.java)

    // ---------------------------------------------------------
    // DOCUMENT
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideDocumentApi(
        retrofit: Retrofit
    ): DocumentApi =
        retrofit.create(DocumentApi::class.java)

    // ---------------------------------------------------------
    // LIMIT
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideLimitApi(
        retrofit: Retrofit
    ): LimitApi =
        retrofit.create(LimitApi::class.java)

    // ---------------------------------------------------------
    // LOAN APPLICATION
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideLoanApplicationApi(
        retrofit: Retrofit
    ): LoanApplicationApi =
        retrofit.create(LoanApplicationApi::class.java)

    // ---------------------------------------------------------
    // INSTALLMENT
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideLoanInstallmentApi(
        retrofit: Retrofit
    ): LoanInstallmentApi =
        retrofit.create(LoanInstallmentApi::class.java)

    // ---------------------------------------------------------
    // TRANSACTION HISTORY
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideTransactionHistoryApi(
        retrofit: Retrofit
    ): TransactionHistoryApi =
        retrofit.create(TransactionHistoryApi::class.java)

    // ---------------------------------------------------------
    // DISBURSEMENT
    // ---------------------------------------------------------

    @Provides
    @Singleton
    fun provideDisbursementApi(
        retrofit: Retrofit
    ): DisbursementApi =
        retrofit.create(DisbursementApi::class.java)
}