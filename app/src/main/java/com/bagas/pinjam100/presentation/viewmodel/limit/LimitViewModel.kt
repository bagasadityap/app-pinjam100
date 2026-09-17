package com.bagas.pinjam100.presentation.viewmodel.limit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            Log.d("LimitViewModel", "getCustomerLimit customerId=$customerId")

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                limitRepository.getCustomerLimit(customerId)
            }.onSuccess { limit ->
                Log.d("LimitViewModel", "limit=$limit")
                Log.d("LimitViewModel", "limit exists=${limit != null}")

                if (limit != null) {
                    Log.d(
                        "LimitViewModel",
                        "creditLimit=${limit.creditLimit}, availableLimit=${limit.availableLimit}"
                    )
                }

                _uiState.update {
                    it.copy(
                        limit = limit,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { exception ->
                Log.e(
                    "LimitViewModel",
                    "Failed to get customer limit",
                    exception
                )

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