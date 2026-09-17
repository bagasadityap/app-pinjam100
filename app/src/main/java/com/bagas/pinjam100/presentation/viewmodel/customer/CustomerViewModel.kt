package com.bagas.pinjam100.presentation.viewmodel.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getDetailById(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val customer = customerRepository.getDetailById(id)

                _uiState.value = _uiState.value.copy(
                    customer = customer,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
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

            try {
                val result = customerRepository.update(id, customer)

                _uiState.value = _uiState.value.copy(
                    customer = result,
                    isSubmitting = false,
                    successMessage = "Data berhasil diperbarui"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = e.message
                )
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

            try {
                val result = customerRepository.saveOnboarding(id, request)

                _uiState.value = _uiState.value.copy(
                    customer = result,
                    isSubmitting = false,
                    successMessage = "Data onboarding berhasil disimpan"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = e.message
                )
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

            try {
                val result = customerRepository.updateOnboarding(id, request)

                _uiState.value = _uiState.value.copy(
                    customer = result,
                    isSubmitting = false,
                    successMessage = "Data onboarding berhasil diperbarui"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    errorMessage = e.message
                )
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

            try {
                val result = customerRepository.delete(id)

                _uiState.value = _uiState.value.copy(
                    customer = result,
                    isDeleting = false,
                    successMessage = "Customer berhasil dihapus"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    errorMessage = e.message
                )
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