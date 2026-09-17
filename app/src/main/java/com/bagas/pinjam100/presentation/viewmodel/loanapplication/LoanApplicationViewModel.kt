package com.bagas.pinjam100.presentation.viewmodel.loanapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

            runCatching {
                createLoanApplicationUseCase(
                    customerId = customerId,
                    loanAmount = loanAmount,
                    tenorMonths = tenorMonths,
                    purpose = purpose
                )
            }.onSuccess { application ->
                _uiState.update {
                    it.copy(
                        createdLoanApplication = application,
                        isSubmitting = false,
                        errorMessage = null
                    )
                }

                onSuccess()
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = exception.message
                    )
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

            runCatching {
                getCustomerLoanApplicationsUseCase(customerId)
            }.onSuccess { applications ->
                _uiState.update {
                    it.copy(
                        loanApplications = applications,
                        isLoading = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                    )
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

            runCatching {
                getLoanApplicationByIdUseCase(id)
            }.onSuccess { application ->
                _uiState.update {
                    it.copy(
                        selectedLoanApplication = application,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                    )
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