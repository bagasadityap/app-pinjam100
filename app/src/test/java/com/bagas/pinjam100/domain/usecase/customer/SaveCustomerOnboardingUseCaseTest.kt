package com.bagas.pinjam100.domain.usecase.customer

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding
import com.bagas.pinjam100.domain.repository.CustomerRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveCustomerOnboardingUseCaseTest {
    private lateinit var customerRepository: CustomerRepository
    private lateinit var saveCustomerOnboardingUseCase: SaveCustomerOnboardingUseCase

    @Before
    fun setUp() {
        customerRepository = mockk()
        saveCustomerOnboardingUseCase = SaveCustomerOnboardingUseCase(customerRepository)
    }

    @Test
    fun `invoke should call saveOnboarding in repository and return the exact AppResult`() {
        runTest {
            val customerId = "CUST-123"
            val mockRequest = mockk<CustomerOnboarding>()
            val expectedResult = mockk<AppResult<Customer>>()

            coEvery {
                customerRepository.saveOnboarding(id = customerId, request = mockRequest)
            } returns expectedResult

            val actualResult = saveCustomerOnboardingUseCase(
                customerId = customerId,
                request = mockRequest
            )

            assertEquals(expectedResult, actualResult)

            coVerify(exactly = 1) {
                customerRepository.saveOnboarding(id = customerId, request = mockRequest)
            }
        }
    }
}