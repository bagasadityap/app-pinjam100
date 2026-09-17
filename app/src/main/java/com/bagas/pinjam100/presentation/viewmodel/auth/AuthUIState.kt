package com.bagas.pinjam100.presentation.viewmodel.auth

import com.bagas.pinjam100.core.network.AppFailure
import com.bagas.pinjam100.domain.model.auth.AuthUser

enum class AuthStatus {
    UNKNOWN,
    AUTHENTICATED,
    UNAUTHENTICATED,
}

data class AuthUiState(
    val status: AuthStatus = AuthStatus.UNKNOWN,
    val user: AuthUser? = null,
    val isSubmitting: Boolean = false,
    val isResendingOtp: Boolean = false,
    val failure: AppFailure? = null,
    val errorMessage: String? = null,
) {
    val isLoggedIn: Boolean get() = status == AuthStatus.AUTHENTICATED

    val isRestoringSession: Boolean get() = status == AuthStatus.UNKNOWN

    val isLoggedOut: Boolean get() = status == AuthStatus.UNAUTHENTICATED
}