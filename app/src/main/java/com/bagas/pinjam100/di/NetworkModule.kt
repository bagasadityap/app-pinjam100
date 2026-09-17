package com.bagas.pinjam100.di

import android.content.Context
import com.bagas.pinjam100.BuildConfig
import com.bagas.pinjam100.core.network.AuthHeaderInterceptor
import com.bagas.pinjam100.core.network.AuthTokenProvider
import com.bagas.pinjam100.data.auth.local.AuthSessionLocalDataSource
import com.bagas.pinjam100.data.auth.local.SessionAuthTokenProvider
import com.bagas.pinjam100.data.auth.remote.AuthApi
import com.bagas.pinjam100.data.customer.remote.CustomerApi
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
import javax.inject.Singleton

private const val TIMEOUT_SECONDS = 30L
private const val HEADER_AUTHORIZATION = "Authorization"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideAuthTokenProvider(
        localDataSource: AuthSessionLocalDataSource,
    ): AuthTokenProvider = SessionAuthTokenProvider(localDataSource)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        tokenProvider: AuthTokenProvider,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(AuthHeaderInterceptor(tokenProvider))
        // In release builds, the chucker-no-op artifact ensures that this interceptor has no effect.
        .addInterceptor(
            ChuckerInterceptor.Builder(context)
                .redactHeaders(HEADER_AUTHORIZATION)
                .alwaysReadResponseBody(true)
                .build()
        )
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    object WilayahModule {

        @Provides
        @Singleton
        fun provideWilayahApi(
            retrofit: Retrofit
        ): WilayahApi {
            return retrofit.newBuilder()
                .baseUrl("https://wilayah.id/api/")
                .build()
                .create(WilayahApi::class.java)
        }

        @Provides
        @Singleton
        fun provideWilayahRepository(
            api: WilayahApi
        ): WilayahRepository {
            return WilayahRepositoryImpl(api)
        }
    }

    @Provides
    @Singleton
    fun provideCustomerApi(
        retrofit: Retrofit
    ): CustomerApi {
        return retrofit.create(CustomerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDocumentApi(
        retrofit: Retrofit
    ): DocumentApi {
        return retrofit.create(DocumentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLimitApi(
        retrofit: Retrofit
    ): LimitApi {
        return retrofit.create(LimitApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoanApplicationApi(
        retrofit: Retrofit
    ): LoanApplicationApi {
        return retrofit.create(LoanApplicationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoanInstallmentApi(
        retrofit: Retrofit
    ): LoanInstallmentApi {
        return retrofit.create(LoanInstallmentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionHistoryApi(
        retrofit: Retrofit
    ): TransactionHistoryApi {
        return retrofit.create(TransactionHistoryApi::class.java)
    }
}