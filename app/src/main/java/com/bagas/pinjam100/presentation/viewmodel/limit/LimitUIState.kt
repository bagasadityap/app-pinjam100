package com.bagas.pinjam100.presentation.viewmodel.limit

import com.bagas.pinjam100.domain.model.limit.Limit

data class LimitUIState(
    val limit: Limit? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)