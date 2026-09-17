package com.bagas.pinjam100.domain.repository

import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding

interface CustomerRepository {

    suspend fun getDetailById(id: String): Customer

    suspend fun update(
        id: String,
        request: Customer
    ): Customer

    suspend fun saveOnboarding(
        id: String,
        request: CustomerOnboarding
    ): Customer

    suspend fun updateOnboarding(
        id: String,
        request: CustomerOnboarding
    ): Customer

    suspend fun delete(id: String): Customer
}