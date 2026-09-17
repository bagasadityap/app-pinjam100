package com.bagas.pinjam100.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bagas.pinjam100.data.customer.local.CustomerDao
import com.bagas.pinjam100.data.document.local.DocumentDao
import com.bagas.pinjam100.data.loanapplication.local.LoanApplicationDao
import com.bagas.pinjam100.data.installment.local.LoanInstallmentDao
import com.bagas.pinjam100.data.rekening.local.RekeningDao
import com.bagas.pinjam100.data.customer.local.CustomerEntity
import com.bagas.pinjam100.data.document.local.DocumentEntity
import com.bagas.pinjam100.data.loanapplication.local.LoanApplicationEntity
import com.bagas.pinjam100.data.installment.local.LoanInstallmentEntity
import com.bagas.pinjam100.data.rekening.local.RekeningEntity
import com.bagas.pinjam100.data.transaction.local.TransactionHistoryDao
import com.bagas.pinjam100.data.transaction.local.TransactionHistoryEntity

@Database(
    entities = [
        CustomerEntity::class,
        DocumentEntity::class,
        RekeningEntity::class,
        LoanApplicationEntity::class,
        LoanInstallmentEntity::class,
        TransactionHistoryEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun documentDao(): DocumentDao
    abstract fun rekeningDao(): RekeningDao
    abstract fun loanApplicationDao(): LoanApplicationDao
    abstract fun loanInstallmentDao(): LoanInstallmentDao
    abstract fun transactionHistoryDao(): TransactionHistoryDao

    companion object {
        const val NAME = "pinjam100.db"
    }
}