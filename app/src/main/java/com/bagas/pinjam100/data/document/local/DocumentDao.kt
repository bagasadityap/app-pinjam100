package com.bagas.pinjam100.data.document.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {

    @Query("SELECT * FROM document WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<DocumentEntity?>

    @Query("SELECT * FROM document WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): DocumentEntity?

    @Query("SELECT * FROM document")
    fun observeAll(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM document WHERE customerId = :customerId")
    fun observeByCustomerId(customerId: String): Flow<List<DocumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: DocumentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(documents: List<DocumentEntity>)

    @Delete
    suspend fun delete(document: DocumentEntity)

    @Query("DELETE FROM document")
    suspend fun deleteAll()
}