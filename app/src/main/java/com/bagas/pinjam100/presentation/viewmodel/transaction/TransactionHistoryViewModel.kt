package com.bagas.pinjam100.presentation.viewmodel.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.core.error.AppResult
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
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.failure.toMessage()
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