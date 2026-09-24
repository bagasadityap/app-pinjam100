package com.bagas.pinjam100.presentation.viewmodel.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding
import com.bagas.pinjam100.domain.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerUIState())
    val uiState: StateFlow<CustomerUIState> = _uiState.asStateFlow()

    private val _detailUiState = MutableStateFlow(CustomerDetailUIState())
    val detailUiState: StateFlow<CustomerDetailUIState> = _detailUiState.asStateFlow()

    fun getDetailById(id: String) {
        viewModelScope.launch {
            _detailUiState.value = _detailUiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (
                val result = customerRepository.getDetailById(id)
            ) {
                is AppResult.Success -> {
                    _detailUiState.value =
                        _detailUiState.value.copy(
                            customerDetail = result.data,
                            isLoading = false
                        )
                }

                is AppResult.Failure -> {
                    _detailUiState.value =
                        _detailUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.failure.toString()
                        )
                }
            }
        }
    }

    fun update(
        id: String,
        customer: Customer
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = customerRepository.update(id, customer)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        customer = result.data,
                        isSubmitting = false,
                        successMessage = "Data berhasil diperbarui"
                    )
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = result.failure.toString()
                    )
                }
            }
        }
    }

    fun saveOnboarding(
        id: String,
        request: CustomerOnboarding
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = customerRepository.saveOnboarding(id, request)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        customer = result.data,
                        isSubmitting = false,
                        successMessage = "Data onboarding berhasil disimpan"
                    )
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = result.failure.toString()
                    )
                }
            }
        }
    }

    fun updateOnboarding(
        id: String,
        request: CustomerOnboarding
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSubmitting = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = customerRepository.updateOnboarding(id, request)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        customer = result.data,
                        isSubmitting = false,
                        successMessage = "Data onboarding berhasil diperbarui"
                    )
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = result.failure.toString()
                    )
                }
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                errorMessage = null,
                successMessage = null
            )

            when (val result = customerRepository.delete(id)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        customer = result.data,
                        isDeleting = false,
                        successMessage = "Customer berhasil dihapus"
                    )
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        errorMessage = result.failure.toString()
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(
            successMessage = null
        )
    }
}