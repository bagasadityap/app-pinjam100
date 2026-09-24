package com.bagas.pinjam100.data.limit.repository

import com.bagas.pinjam100.core.error.ApiEnvelope
import com.bagas.pinjam100.data.limit.local.LimitDao
import com.bagas.pinjam100.data.limit.remote.LimitApi
import com.bagas.pinjam100.data.limit.remote.LimitResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class LimitRepositoryImplTest {

    private lateinit var api: LimitApi
    private lateinit var dao: LimitDao
    private lateinit var json: Json
    private lateinit var repository: LimitRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        dao = mockk(relaxed = true)
        json = Json { ignoreUnknownKeys = true }

        repository = LimitRepositoryImpl(api = api, dao = dao, json = json)
    }

    @Test
    fun `getCustomerLimit should call api getCustomerLimit`() {
        runTest {
            val customerId = "CUST-123"

            coEvery { api.getCustomerLimit(customerId) } returns ApiEnvelope(
                data = LimitResponse(
                    id = "1",
                    customerId = customerId,
                    creditLimit = 10000000.00,
                    availableLimit = 5000000.00,
                    createdDate = "2026-01-01",
                    updatedDate = "2026-01-01"
                )
            )

            repository.getCustomerLimit(customerId)

            coVerify(exactly = 1) {
                api.getCustomerLimit(customerId)
            }
        }
    }

    @Test
    fun `getCustomerLimit should save to local dao using upsert when api succeeds`() {
        runTest {
            val customerId = "CUST-123"

            val limitResponse = LimitResponse(
                id = "1",
                customerId = customerId,
                creditLimit = 10000000.00,
                availableLimit = 5000000.00,
                createdDate = "2026-01-01",
                updatedDate = "2026-01-01"
            )

            coEvery { api.getCustomerLimit(customerId) } returns ApiEnvelope(
                data = limitResponse
            )

            repository.getCustomerLimit(customerId)

            coVerify(exactly = 1) { dao.upsert(any()) }
            coVerify(exactly = 0) { dao.getByCustomerId(any()) }
        }
    }

    @Test
    fun `getCustomerLimit should fallback to dao getByCustomerId when api fails`() {
        runTest {
            val customerId = "CUST-123"

            coEvery {
                api.getCustomerLimit(customerId)
            } throws Exception("Network Error")

            repository.getCustomerLimit(customerId)

            coVerify(exactly = 0) {
                dao.upsert(any())
            }

            coVerify(exactly = 1) {
                dao.getByCustomerId(customerId)
            }
        }
    }
}