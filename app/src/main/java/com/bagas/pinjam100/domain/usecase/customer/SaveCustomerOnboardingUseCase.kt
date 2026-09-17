package com.bagas.pinjam100.domain.usecase.customer

import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding
import com.bagas.pinjam100.domain.repository.CustomerRepository
import javax.inject.Inject

class SaveCustomerOnboardingUseCase @Inject constructor(
    private val customerRepository: CustomerRepository
) {

    suspend operator fun invoke(
        customerId: String,
        request: CustomerOnboarding
    ): Customer {
        return customerRepository.saveOnboarding(
            id = customerId,
            request = request
        )
    }
}