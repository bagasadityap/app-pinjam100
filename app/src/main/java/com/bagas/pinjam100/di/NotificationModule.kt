package com.bagas.pinjam100.di

import android.content.Context
import com.bagas.pinjam100.MainActivity
import com.bagas.pinjam100.core.notification.AndroidAppNotifier
import com.bagas.pinjam100.core.notification.AppNotifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideAppNotifier(
        @ApplicationContext context: Context
    ): AppNotifier =
        AndroidAppNotifier(
            context = context,
            target = MainActivity::class.java
        )
}