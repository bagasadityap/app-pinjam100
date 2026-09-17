package com.bagas.pinjam100.presentation.viewmodel.document

import com.bagas.pinjam100.data.document.remote.DocumentResponse

data class DocumentUIState(
    val documents: List<DocumentResponse> = emptyList(),
    val isUploading: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)