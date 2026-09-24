package com.bagas.pinjam100.presentation.viewmodel.document

import com.bagas.pinjam100.domain.model.document.Document

data class DocumentUIState(
    val documents: List<Document> = emptyList(),
    val isUploading: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)