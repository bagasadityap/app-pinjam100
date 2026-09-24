package com.bagas.pinjam100.presentation.viewmodel.loanapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.usecase.loanapplication.CreateLoanApplicationUseCase
import com.bagas.pinjam100.domain.usecase.loanapplication.GetCustomerLoanApplicationsUseCase
import com.bagas.pinjam100.domain.usecase.loanapplication.GetLoanApplicationByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoanApplicationViewModel @Inject constructor(
    private val createLoanApplicationUseCase: CreateLoanApplicationUseCase,
    private val getCustomerLoanApplicationsUseCase: GetCustomerLoanApplicationsUseCase,
    private val getLoanApplicationByIdUseCase: GetLoanApplicationByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoanApplicationUIState())
    val uiState: StateFlow<LoanApplicationUIState> = _uiState.asStateFlow()

    fun create(
        customerId: String,
        loanAmount: Long,
        tenorMonths: Int,
        purpose: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null
                )
            }

            when (
                val result = createLoanApplicationUseCase(
                    customerId = customerId,
                    loanAmount = loanAmount,
                    tenorMonths = tenorMonths,
                    purpose = purpose
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            createdLoanApplication = result.data,
                            isSubmitting = false,
                            errorMessage = null
                        )
                    }

                    onSuccess()
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = "Gagal membuat aplikasi pinjaman"
                        )
                    }
                }
            }
        }
    }

    fun getByCustomer(customerId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = getCustomerLoanApplicationsUseCase(customerId)) {

                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            loanApplications = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Gagal mengambil data aplikasi pinjaman"
                        )
                    }
                }
            }
        }
    }

    fun getById(id: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = getLoanApplicationByIdUseCase(id)) {

                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            selectedLoanApplication = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Gagal mengambil data aplikasi pinjaman"
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}