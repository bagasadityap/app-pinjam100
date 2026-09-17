package com.bagas.pinjam100.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.core.error.AppResult
import com.bagas.pinjam100.domain.model.auth.ChangePasswordData
import com.bagas.pinjam100.domain.model.auth.ForgotPasswordData
import com.bagas.pinjam100.domain.model.auth.LoginCredentials
import com.bagas.pinjam100.domain.model.auth.RegisterData
import com.bagas.pinjam100.domain.model.auth.ResendOtpData
import com.bagas.pinjam100.domain.model.auth.ResetPasswordData
import com.bagas.pinjam100.domain.model.auth.VerifyOtpData
import com.bagas.pinjam100.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.observeSession().collect { session ->
                _uiState.update {
                    it.copy(
                        status = if (session == null) {
                            AuthStatus.UNAUTHENTICATED
                        } else {
                            AuthStatus.AUTHENTICATED
                        },
                        user = session?.user
                    )
                }
            }
        }
    }

    fun login(phoneNumber: String, password: String) {
        if (_uiState.value.isSubmitting) return

        if (phoneNumber.isBlank() || password.isBlank()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Nomor telepon dan password tidak boleh kosong"
                )
            }
            return
        }

        val credentials = LoginCredentials(
            phoneNumber = normalizePhoneNumber(phoneNumber),
            password = password
        )

        viewModelScope.launch {
            markSubmitting()

            when (val result = authRepository.login(credentials)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = null,
                            status = AuthStatus.AUTHENTICATED,
                            user = result.data.user
                        )
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.failure.toMessage(),
                            status = AuthStatus.UNAUTHENTICATED
                        )
                    }
                }
            }
            markIdle()
        }
    }

    fun register(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        if (_uiState.value.isSubmitting) return

        when {
            fullName.isBlank() ||
                    phoneNumber.isBlank() ||
                    email.isBlank() ||
                    password.isBlank() ||
                    confirmPassword.isBlank() -> {
                _uiState.update {
                    it.copy(
                        errorMessage = "Semua field wajib diisi"
                    )
                }
                return
            }

            password != confirmPassword -> {
                _uiState.update {
                    it.copy(
                        errorMessage = "Konfirmasi password tidak sesuai"
                    )
                }
                return
            }
        }

        val data = RegisterData(
            fullName = fullName.trim(),
            phoneNumber = normalizePhoneNumber(phoneNumber),
            email = email.trim(),
            password = password
        )

        viewModelScope.launch {
            markSubmitting()

            when (val result = authRepository.register(data)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = null
                        )
                    }

                    onSuccess()
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.failure.toMessage()
                        )
                    }
                }
            }

            markIdle()
        }
    }

    fun verifyOtp(
        phoneNumber: String,
        otpCode: String,
        onSuccess: () -> Unit
    ) {
        if (_uiState.value.isSubmitting) return

        if (otpCode.length != 6) {
            _uiState.update {
                it.copy(
                    errorMessage = "Kode OTP harus 6 digit"
                )
            }
            return
        }

        viewModelScope.launch {
            markSubmitting()

            when (
                val result = authRepository.verifyOtp(
                    VerifyOtpData(
                        phoneNumber = phoneNumber,
                        otpCode = otpCode
                    )
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            status = AuthStatus.AUTHENTICATED,
                            user = result.data.user,
                            errorMessage = null
                        )
                    }

                    onSuccess()
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.failure.toMessage()
                        )
                    }
                }
            }

            markIdle()
        }
    }

    fun resendOtp(
        phoneNumber: String,
        onSuccess: () -> Unit
    ) {
        if (_uiState.value.isSubmitting) return

        viewModelScope.launch {
            markSubmitting()

            when (val result = authRepository.resendOtp(ResendOtpData(phoneNumber))) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(errorMessage = null)
                    }
                    onSuccess()
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.failure.toMessage()
                        )
                    }
                }
            }

            markIdle()
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        if (_uiState.value.isSubmitting) return

        when {
            currentPassword.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "Password saat ini tidak boleh kosong")
                }
                return
            }

            newPassword.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "Password baru tidak boleh kosong")
                }
                return
            }

            confirmPassword.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "Konfirmasi password tidak boleh kosong")
                }
                return
            }

            newPassword != confirmPassword -> {
                _uiState.update {
                    it.copy(errorMessage = "Password tidak sama")
                }
                return
            }
        }

        viewModelScope.launch {
            markSubmitting()

            when (
                val result = authRepository.changePassword(
                    ChangePasswordData(
                        currentPassword = currentPassword,
                        newPassword = newPassword
                    )
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(errorMessage = null)
                    }

                    onSuccess()
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.failure.toMessage()
                        )
                    }
                }
            }

            markIdle()
        }
    }

    fun forgotPassword(
        email: String
    ) {
        if (_uiState.value.isSubmitting) return

        if (email.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Email tidak boleh kosong")
            }
            return
        }

        viewModelScope.launch {
            markSubmitting()

            when (val result = authRepository.forgotPassword(
                ForgotPasswordData(email.trim())
            )) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(errorMessage = null)
                    }
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.failure.toMessage()
                        )
                    }
                }
            }

            markIdle()
        }
    }

    fun resetPassword(
        token: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        if (_uiState.value.isSubmitting) return

        when {
            password.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "Password tidak boleh kosong")
                }
                return
            }

            confirmPassword.isBlank() -> {
                _uiState.update {
                    it.copy(errorMessage = "Konfirmasi password tidak boleh kosong")
                }
                return
            }

            password != confirmPassword -> {
                _uiState.update {
                    it.copy(errorMessage = "Password tidak sama")
                }
                return
            }
        }

        viewModelScope.launch {
            markSubmitting()

            when (
                val result = authRepository.resetPassword(
                    ResetPasswordData(
                        token = token,
                        password = password
                    )
                )
            ) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(errorMessage = null)
                    }

                    onSuccess()
                }

                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.failure.toMessage()
                        )
                    }
                }
            }

            markIdle()
        }
    }

    private fun markSubmitting() {
        _uiState.update {
            it.copy(
                isSubmitting = true,
                errorMessage = null
            )
        }
    }

    private fun markIdle() {
        _uiState.update {
            it.copy(isSubmitting = false)
        }
    }

    private fun normalizePhoneNumber(phoneNumber: String): String {
        val value = phoneNumber.trim()

        return when {
            value.startsWith("62") -> value
            value.startsWith("0") -> "62${value.drop(1)}"
            else -> "62$value"
        }
    }

    fun markProfileCompleted() {
        _uiState.update { state ->
            state.copy(
                user = state.user?.copy(
                    profileCompleted = true
                )
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()

            _uiState.update {
                it.copy(
                    status = AuthStatus.UNAUTHENTICATED,
                    user = null,
                    errorMessage = null
                )
            }
        }
    }
}