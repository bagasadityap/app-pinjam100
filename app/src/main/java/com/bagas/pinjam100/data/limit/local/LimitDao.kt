package com.bagas.pinjam100.data.limit.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LimitDao {

    @Query("""
        SELECT * 
        FROM limits 
        WHERE customerId = :customerId
        LIMIT 1
    """)
    suspend fun getByCustomerId(customerId: String): LimitEntity?

    @Upsert
    suspend fun upsert(limit: LimitEntity)

    @Query("DELETE FROM limits WHERE customerId = :customerId")
    suspend fun deleteByCustomerId(customerId: String)
}