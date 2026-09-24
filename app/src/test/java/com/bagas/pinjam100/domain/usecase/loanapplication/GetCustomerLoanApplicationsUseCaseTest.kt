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

class GetCustomerLoanApplicationsUseCaseTest {

    private lateinit var repository: LoanApplicationRepository
    private lateinit var getCustomerLoanApplicationsUseCase: GetCustomerLoanApplicationsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getCustomerLoanApplicationsUseCase = GetCustomerLoanApplicationsUseCase(repository)
    }

    @Test
    fun `invoke should call getByCustomer in repository and return exact AppResult`() {
        runTest {
            val customerId = "CUST-123"
            val expectedResult = mockk<AppResult<List<LoanApplication>>>()

            coEvery {
                repository.getByCustomer(customerId)
            } returns expectedResult

            val actualResult = getCustomerLoanApplicationsUseCase(customerId)

            assertEquals(expectedResult, actualResult)

            coVerify(exactly = 1) {
                repository.getByCustomer(customerId)
            }
        }
    }
}