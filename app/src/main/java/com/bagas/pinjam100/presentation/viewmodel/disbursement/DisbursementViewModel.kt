package com.bagas.pinjam100.presentation.viewmodel.disbursement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.repository.DisbursementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisbursementViewModel @Inject constructor(
    private val repository: DisbursementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisbursementUIState())
    val uiState: StateFlow<DisbursementUIState> = _uiState.asStateFlow()

    fun getById(id: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = repository.getById(id)) {

                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            disbursement = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Gagal mengambil data pencairan"
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