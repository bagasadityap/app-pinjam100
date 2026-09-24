package com.bagas.pinjam100.presentation.viewmodel.installment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.installment.InstallmentStatus
import com.bagas.pinjam100.domain.repository.LoanInstallmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class LoanInstallmentViewModel @Inject constructor(
    private val repository: LoanInstallmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoanInstallmentUIState())
    val uiState: StateFlow<LoanInstallmentUIState> = _uiState.asStateFlow()

    fun getById(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = repository.getById(id)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        selectedInstallment = result.data,
                        isLoading = false
                    )
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal mengambil data angsuran"
                    )
                }
            }
        }
    }

    fun getByLoanApplicationId(loanApplicationId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = repository.getByLoanApplicationId(loanApplicationId)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        installments = result.data,
                        isLoading = false
                    )
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal mengambil data angsuran"
                    )
                }
            }
        }
    }

    fun getByCustomerId(customerId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            when (val result = repository.getByCustomerId(customerId)) {
                is AppResult.Success -> {
                    val today = LocalDate.now()
                    val maxDate = today.plusDays(30)

                    val installments = result.data
                        .asSequence()
                        .filter {
                            it.status == InstallmentStatus.UNPAID
                        }
                        .filter {
                            try {
                                val dueDate = LocalDate.parse(it.dueDate)
                                dueDate <= maxDate
                            } catch (_: Exception) {
                                false
                            }
                        }
                        .distinctBy { it.id }
                        .sortedBy {
                            LocalDate.parse(it.dueDate)
                        }
                        .toList()

                    _uiState.value = _uiState.value.copy(
                        installments = installments,
                        isLoading = false
                    )
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal memuat tagihan"
                    )
                }
            }
        }
    }

    fun pay(
        id: String,
        customerId: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isPaying = true,
                errorMessage = null,
                paymentSuccess = false
            )

            when (repository.pay(id)) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isPaying = false,
                        paymentSuccess = true
                    )

                    getByCustomerId(customerId)
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isPaying = false,
                        errorMessage = "Gagal melakukan pembayaran"
                    )
                }
            }
        }
    }

    fun clearPaymentSuccess() {
        _uiState.value = _uiState.value.copy(
            paymentSuccess = false
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}