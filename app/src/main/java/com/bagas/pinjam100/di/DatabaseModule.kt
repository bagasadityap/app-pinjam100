package com.bagas.pinjam100.di

import android.content.Context
import androidx.room.Room
import com.bagas.pinjam100.core.database.AppDatabase
import com.bagas.pinjam100.data.customer.local.CustomerDao
import com.bagas.pinjam100.data.document.local.DocumentDao
import com.bagas.pinjam100.data.loanapplication.local.LoanApplicationDao
import com.bagas.pinjam100.data.installment.local.LoanInstallmentDao
import com.bagas.pinjam100.data.rekening.local.RekeningDao
import com.bagas.pinjam100.data.transaction.local.TransactionHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.NAME
        )
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .build()

    @Provides
    fun provideCustomerDao(
        database: AppDatabase
    ): CustomerDao = database.customerDao()

    @Provides
    fun provideDocumentDao(
        database: AppDatabase
    ): DocumentDao = database.documentDao()

    @Provides
    fun provideRekeningDao(
        database: AppDatabase
    ): RekeningDao = database.rekeningDao()

    @Provides
    fun provideLoanApplicationDao(
        database: AppDatabase
    ): LoanApplicationDao = database.loanApplicationDao()

    @Provides
    fun provideLoanInstallmentDao(
        database: AppDatabase
    ): LoanInstallmentDao = database.loanInstallmentDao()

    @Provides
    fun provideTransactionHistoryDao(
        database: AppDatabase
    ): TransactionHistoryDao = database.transactionHistoryDao()
}