package com.bagas.pinjam100.data.customer.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.error.map
import com.bagas.pinjam100.core.error.requirePayload
import com.bagas.pinjam100.core.error.runApiCatching
import com.bagas.pinjam100.data.customer.remote.CustomerApi
import com.bagas.pinjam100.data.customer.remote.CustomerRequest
import com.bagas.pinjam100.data.customer.remote.toDomain
import com.bagas.pinjam100.data.customer.remote.toRequest
import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding
import com.bagas.pinjam100.domain.repository.CustomerRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val api: CustomerApi,
    private val json: Json
) : CustomerRepository {

    override suspend fun getDetailById(id: String): AppResult<Customer> {
        return runApiCatching(json) {
            api.getDetailById(id)
                .requirePayload()
                .map { it.toDomain() }
        }
    }

    override suspend fun update(
        id: String,
        request: Customer
    ): AppResult<Customer> {
        return runApiCatching(json) {
            api.update(
                id = id,
                request = CustomerRequest(
                    fullName = request.fullName,
                    email = request.email,
                    phoneNumber = request.phoneNumber
                )
            )
                .requirePayload()
                .map { it.toDomain() }
        }
    }

    override suspend fun saveOnboarding(
        id: String,
        request: CustomerOnboarding
    ): AppResult<Customer> {
        return runApiCatching(json) {
            api.saveOnboarding(
                id = id,
                request = request.toRequest()
            )
                .requirePayload()
                .map { it.toDomain() }
        }
    }

    override suspend fun updateOnboarding(
        id: String,
        request: CustomerOnboarding
    ): AppResult<Customer> {
        return runApiCatching(json) {
            api.updateOnboarding(
                id = id,
                request = request.toRequest()
            )
                .requirePayload()
                .map { it.toDomain() }
        }
    }

    override suspend fun delete(id: String): AppResult<Customer> {
        return runApiCatching(json) {
            api.delete(id)
                .requirePayload()
                .map { it.toDomain() }
        }
    }
}