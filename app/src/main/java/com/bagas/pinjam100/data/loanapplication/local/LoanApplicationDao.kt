package com.bagas.pinjam100.data.loanapplication.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanApplicationDao {

    @Query("SELECT * FROM loan_application WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<LoanApplicationEntity?>

    @Query("SELECT * FROM loan_application WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): LoanApplicationEntity?

    @Query("SELECT * FROM loan_application")
    fun observeAll(): Flow<List<LoanApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loanApplication: LoanApplicationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(loanApplications: List<LoanApplicationEntity>)

    @Delete
    suspend fun delete(loanApplication: LoanApplicationEntity)

    @Query("DELETE FROM loan_application")
    suspend fun deleteAll()
}