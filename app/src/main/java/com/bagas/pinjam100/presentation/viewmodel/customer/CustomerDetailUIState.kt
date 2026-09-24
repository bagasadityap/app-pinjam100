package com.bagas.pinjam100.presentation.viewmodel.customer

import com.bagas.pinjam100.domain.model.customer.CustomerDetail

data class CustomerDetailUIState(
    val customerDetail: CustomerDetail? = null,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)