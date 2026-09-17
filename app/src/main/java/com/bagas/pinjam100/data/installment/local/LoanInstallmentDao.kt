package com.bagas.pinjam100.data.installment.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanInstallmentDao {

    @Query("SELECT * FROM loan_installment WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<LoanInstallmentEntity?>

    @Query("SELECT * FROM loan_installment WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): LoanInstallmentEntity?

    @Query("SELECT * FROM loan_installment")
    fun observeAll(): Flow<List<LoanInstallmentEntity>>

    @Query("""
        SELECT * FROM loan_installment
        WHERE loanApplicationId = :loanApplicationId
        ORDER BY installmentNumber ASC
    """)
    fun observeByLoanApplicationId(
        loanApplicationId: String
    ): Flow<List<LoanInstallmentEntity>>

    @Query("""
        SELECT * FROM loan_installment
        WHERE loanApplicationId = :loanApplicationId
        ORDER BY installmentNumber ASC
    """)
    suspend fun getByLoanApplicationId(
        loanApplicationId: String
    ): List<LoanInstallmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loanInstallment: LoanInstallmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(loanInstallments: List<LoanInstallmentEntity>)

    @Delete
    suspend fun delete(loanInstallment: LoanInstallmentEntity)

    @Query("DELETE FROM loan_installment")
    suspend fun deleteAll()

    @Query("""
        DELETE FROM loan_installment
        WHERE loanApplicationId = :loanApplicationId
    """)
    suspend fun deleteByLoanApplicationId(
        loanApplicationId: String
    )

    @Query(
        """
    DELETE FROM loan_installment
    WHERE loanApplicationId IN (
        SELECT id
        FROM loan_application
        WHERE customerId = :customerId
    )
    """
    )
    suspend fun deleteByCustomerId(customerId: String)
}