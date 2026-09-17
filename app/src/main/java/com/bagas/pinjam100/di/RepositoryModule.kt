package com.bagas.pinjam100.di

import com.bagas.pinjam100.data.customer.repository.CustomerRepositoryImpl
import com.bagas.pinjam100.data.document.repository.DocumentRepositoryImpl
import com.bagas.pinjam100.data.installment.repository.LoanInstallmentRepositoryImpl
import com.bagas.pinjam100.data.limit.repository.LimitRepositoryImpl
import com.bagas.pinjam100.data.loanapplication.repository.LoanApplicationRepositoryImpl
import com.bagas.pinjam100.data.transaction.repository.TransactionHistoryRepositoryImpl
import com.bagas.pinjam100.domain.repository.CustomerRepository
import com.bagas.pinjam100.domain.repository.DocumentRepository
import com.bagas.pinjam100.domain.repository.LimitRepository
import com.bagas.pinjam100.domain.repository.LoanApplicationRepository
import com.bagas.pinjam100.domain.repository.LoanInstallmentRepository
import com.bagas.pinjam100.domain.repository.TransactionHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        implementation: CustomerRepositoryImpl
    ): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindDocumentRepository(
        implementation: DocumentRepositoryImpl
    ): DocumentRepository

    @Binds
    @Singleton
    abstract fun bindLimitRepository(
        implementation: LimitRepositoryImpl
    ): LimitRepository

    @Binds
    @Singleton
    abstract fun bindLoanApplicationRepository(
        repository: LoanApplicationRepositoryImpl
    ): LoanApplicationRepository

    @Binds
    @Singleton
    abstract fun bindLoanInstallmentRepository(
        repository: LoanInstallmentRepositoryImpl
    ): LoanInstallmentRepository

    @Binds
    @Singleton
    abstract fun bindTransactioHistoryRepository(
        repository: TransactionHistoryRepositoryImpl
    ): TransactionHistoryRepository
}