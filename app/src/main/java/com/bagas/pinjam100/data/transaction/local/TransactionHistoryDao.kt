package com.bagas.pinjam100.data.transaction.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TransactionHistoryDao {

    @Query(
        """
        SELECT * FROM transaction_history
        ORDER BY date DESC
        """
    )
    suspend fun getAll(): List<TransactionHistoryEntity>

    @Query(
        """
        SELECT * FROM transaction_history
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(id: String): TransactionHistoryEntity?

    @Query("DELETE FROM transaction_history")
    suspend fun deleteAll()
}