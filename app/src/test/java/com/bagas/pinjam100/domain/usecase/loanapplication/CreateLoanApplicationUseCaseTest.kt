package com.bagas.pinjam100.domain.usecase.loanapplication

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication
import com.bagas.pinjam100.domain.repository.LoanApplicationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreateLoanApplicationUseCaseTest {
    private lateinit var repository: LoanApplicationRepository
    private lateinit var createLoanApplicationUseCase: CreateLoanApplicationUseCase

    @Before
    fun setUp() {
        repository = mockk()
        createLoanApplicationUseCase = CreateLoanApplicationUseCase(repository)
    }

    @Test
    fun `invoke should call create in repository with correct params and return exact AppResult`() {
        runTest {
            val customerId = "CUST-999"
            val loanAmount = 5000000L
            val tenorMonths = 6
            val purpose = "Modal Usaha"

            val expectedResult = mockk<AppResult<LoanApplication>>()

            coEvery {
                repository.create(
                    customerId = customerId,
                    loanAmount = loanAmount,
                    tenorMonths = tenorMonths,
                    purpose = purpose
                )
            } returns expectedResult

            val actualResult = createLoanApplicationUseCase(
                customerId = customerId,
                loanAmount = loanAmount,
                tenorMonths = tenorMonths,
                purpose = purpose
            )

            assertEquals(expectedResult, actualResult)

            coVerify(exactly = 1) {
                repository.create(
                    customerId = customerId,
                    loanAmount = loanAmount,
                    tenorMonths = tenorMonths,
                    purpose = purpose
                )
            }
        }
    }
}