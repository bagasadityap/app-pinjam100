package com.bagas.pinjam100.data.loanapplication.repository

import com.bagas.pinjam100.data.loanapplication.remote.LoanApplicationApi
import com.bagas.pinjam100.data.loanapplication.remote.LoanApplicationRequest
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class LoanApplicationRepositoryImplTest {

    private lateinit var api: LoanApplicationApi
    private lateinit var json: Json
    private lateinit var repository: LoanApplicationRepositoryImpl

    @Before
    fun setUp() {
        api = mockk(relaxed = true)
        json = mockk(relaxed = true)
        repository = LoanApplicationRepositoryImpl(
            api = api,
            json = json
        )
    }

    @Test
    fun `getById should call api getById`() {
        runTest {
            val id = "LOAN-123"

            repository.getById(id)

            coVerify(exactly = 1) {
                api.getById(id)
            }
        }
    }

    @Test
    fun `getByCustomer should call api getByCustomer`() {
        runTest {
            val customerId = "CUST-123"

            repository.getByCustomer(customerId)

            coVerify(exactly = 1) {
                api.getByCustomer(customerId)
            }
        }
    }

    @Test
    fun `create should call api create`() {
        runTest {
            val customerId = "CUST-123"
            val loanAmount = 5000000L
            val tenorMonths = 6
            val purpose = "Modal Usaha"

            repository.create(
                customerId = customerId,
                loanAmount = loanAmount,
                tenorMonths = tenorMonths,
                purpose = purpose
            )

            coVerify(exactly = 1) {
                api.create(
                    LoanApplicationRequest(
                        customerId = customerId,
                        loanAmount = loanAmount.toString(),
                        tenorMonths = tenorMonths,
                        purpose = purpose
                    )
                )
            }
        }
    }
}