package com.bagas.pinjam100.presentation.viewmodel.limit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.repository.LimitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LimitViewModel @Inject constructor(
    private val limitRepository: LimitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LimitUIState())
    val uiState: StateFlow<LimitUIState> = _uiState.asStateFlow()

    fun getCustomerLimit(customerId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = limitRepository.getCustomerLimit(customerId)) {

                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            limit = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Gagal mengambil data limit"
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