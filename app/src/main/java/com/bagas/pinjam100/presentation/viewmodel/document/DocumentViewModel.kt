package com.bagas.pinjam100.presentation.viewmodel.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DocumentUIState())
    val uiState: StateFlow<DocumentUIState> = _uiState.asStateFlow()

    fun upload(
        file: MultipartBody.Part,
        type: RequestBody,
        customerId: RequestBody
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            try {
                val document = documentRepository.save(
                    file = file,
                    type = type,
                    customerId = customerId
                )

                _uiState.update {
                    it.copy(
                        documents = it.documents
                            .filterNot { existing -> existing.id == document.id }
                            .filterNot { existing -> existing.type == document.type }
                            .plus(document),
                        isUploading = false,
                        successMessage = "Dokumen berhasil diunggah"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isUploading = false,
                        errorMessage = e.message ?: "Gagal mengunggah dokumen"
                    )
                }
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDeleting = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            try {
                documentRepository.delete(id)

                _uiState.update {
                    it.copy(
                        documents = it.documents.filterNot { document -> document.id == id },
                        isDeleting = false,
                        successMessage = "Dokumen berhasil dihapus"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "Gagal menghapus dokumen"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(successMessage = null) }
    }
}