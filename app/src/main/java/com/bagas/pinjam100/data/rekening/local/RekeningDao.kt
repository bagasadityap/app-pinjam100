package com.bagas.pinjam100.data.rekening.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RekeningDao {

    @Query("SELECT * FROM rekening WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<RekeningEntity?>

    @Query("SELECT * FROM rekening WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): RekeningEntity?

    @Query("SELECT * FROM rekening")
    fun observeAll(): Flow<List<RekeningEntity>>

    @Query("SELECT * FROM rekening WHERE customerId = :customerId")
    fun observeByCustomerId(customerId: String): Flow<List<RekeningEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rekening: RekeningEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rekenings: List<RekeningEntity>)

    @Delete
    suspend fun delete(rekening: RekeningEntity)

    @Query("DELETE FROM rekening")
    suspend fun deleteAll()
}