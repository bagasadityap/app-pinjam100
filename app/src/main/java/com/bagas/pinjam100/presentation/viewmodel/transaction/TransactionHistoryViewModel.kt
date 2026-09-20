package com.bagas.pinjam100.presentation.viewmodel.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.core.network.CommonFailure
import com.bagas.pinjam100.domain.repository.TransactionHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val transactionHistoryRepository: TransactionHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TransactionHistoryUIState()
    )

    val uiState: StateFlow<TransactionHistoryUIState> =
        _uiState.asStateFlow()

    fun getByCustomerId(customerId: String) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (
                val result =
                    transactionHistoryRepository.getByCustomerId(
                        customerId
                    )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            transactions = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is AppResult.Failure -> {
                    val message = when (val appFailure = result.failure) {
                        is CommonFailure.ApiError -> {
                            appFailure.message?.takeIf { it.isNotBlank() }
                                ?: appFailure.details.joinToString(", ").ifBlank {
                                    "Gagal mengambil data transaksi, silakan coba beberapa saat lagi"
                                }
                        }
                        is CommonFailure.Unauthorized -> {
                            appFailure.message?.takeIf { it.isNotBlank() }
                                ?: "Akses tidak diizinkan."
                        }
                        is CommonFailure.Network -> "Koneksi internet bermasalah."
                        is CommonFailure.Unexpected -> "Terjadi kesalahan tidak terduga."
                        else -> "Terjadi kesalahan sistem, silakan coba beberapa saat lagi"
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = message
                        )
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }
}