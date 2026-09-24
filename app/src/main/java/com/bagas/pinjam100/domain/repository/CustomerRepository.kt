package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerDetail
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding

interface CustomerRepository {

    suspend fun getDetailById(id: String): AppResult<CustomerDetail>

    suspend fun update(
        id: String,
        request: Customer
    ): AppResult<Customer>

    suspend fun saveOnboarding(
        id: String,
        request: CustomerOnboarding
    ): AppResult<Customer>

    suspend fun updateOnboarding(
        id: String,
        request: CustomerOnboarding
    ): AppResult<Customer>

    suspend fun delete(id: String): AppResult<Customer>
}