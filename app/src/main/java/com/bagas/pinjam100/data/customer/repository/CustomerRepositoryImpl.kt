package com.bagas.pinjam100.data.customer.repository

import com.bagas.pinjam100.data.customer.remote.CustomerApi
import com.bagas.pinjam100.data.customer.remote.CustomerRequest
import com.bagas.pinjam100.data.customer.remote.toDomain
import com.bagas.pinjam100.data.customer.remote.toRequest
import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding
import com.bagas.pinjam100.domain.repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val api: CustomerApi
) : CustomerRepository {

    override suspend fun getDetailById(id: String): Customer {
        return api.getDetailById(id).toDomain()
    }

    override suspend fun update(
        id: String,
        request: Customer
    ): Customer {
        return api.update(
            id = id,
            request = CustomerRequest(
                fullName = request.fullName,
                email = request.email,
                phoneNumber = request.phoneNumber
            )
        ).toDomain()
    }

    override suspend fun saveOnboarding(
        id: String,
        request: CustomerOnboarding
    ): Customer {
        return api.saveOnboarding(
            id = id,
            request = request.toRequest()
        ).toDomain()
    }

    override suspend fun updateOnboarding(
        id: String,
        request: CustomerOnboarding
    ): Customer {
        return api.updateOnboarding(
            id = id,
            request = request.toRequest()
        ).toDomain()
    }

    override suspend fun delete(id: String): Customer {
        return api.delete(id).toDomain()
    }
}