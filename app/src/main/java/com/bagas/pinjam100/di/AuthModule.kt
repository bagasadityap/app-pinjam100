package com.bagas.pinjam100.di

import android.content.Context
import com.bagas.pinjam100.data.auth.local.AuthSessionLocalDataSource
import com.bagas.pinjam100.data.auth.remote.AuthApi
import com.bagas.pinjam100.data.auth.repository.AuthRepositoryImpl
import com.bagas.pinjam100.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthSessionLocalDataSource(
        @ApplicationContext context: Context,
    ): AuthSessionLocalDataSource = AuthSessionLocalDataSource(context)

    @Provides
    @Singleton
    fun provideAuthRepository(
        localDataSource: AuthSessionLocalDataSource,
        authApi: AuthApi,
        json: Json,
    ): AuthRepository = AuthRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = authApi,
        json = json,
    )
}