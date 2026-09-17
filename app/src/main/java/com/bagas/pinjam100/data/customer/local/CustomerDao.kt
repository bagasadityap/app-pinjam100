package com.bagas.pinjam100.data.customer.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customer WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<CustomerEntity?>

    @Query("SELECT * FROM customer WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CustomerEntity?

    @Query("SELECT * FROM customer LIMIT 1")
    fun observe(): Flow<CustomerEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: CustomerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<CustomerEntity>)

    @Delete
    suspend fun delete(customer: CustomerEntity)

    @Query("DELETE FROM customer")
    suspend fun deleteAll()
}