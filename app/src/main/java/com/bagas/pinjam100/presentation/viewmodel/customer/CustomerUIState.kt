package com.bagas.pinjam100.presentation.viewmodel.customer

import com.bagas.pinjam100.domain.model.customer.Customer

data class CustomerUIState(
    val customer: Customer? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)