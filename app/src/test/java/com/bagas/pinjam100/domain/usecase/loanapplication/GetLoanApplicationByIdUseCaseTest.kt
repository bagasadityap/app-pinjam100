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

class GetLoanApplicationByIdUseCaseTest {

    private lateinit var loanApplicationRepository: LoanApplicationRepository
    private lateinit var getLoanApplicationByIdUseCase: GetLoanApplicationByIdUseCase

    @Before
    fun setUp() {
        loanApplicationRepository = mockk()
        getLoanApplicationByIdUseCase = GetLoanApplicationByIdUseCase(loanApplicationRepository)
    }

    @Test
    fun `invoke should call getById in repository and return exact AppResult`() {
        runTest {
            val id = "LOAN-123"
            val expectedResult = mockk<AppResult<LoanApplication>>()

            coEvery {
                loanApplicationRepository.getById(id)
            } returns expectedResult

            val actualResult = getLoanApplicationByIdUseCase(id)

            assertEquals(expectedResult, actualResult)

            coVerify(exactly = 1) {
                loanApplicationRepository.getById(id)
            }
        }
    }
}