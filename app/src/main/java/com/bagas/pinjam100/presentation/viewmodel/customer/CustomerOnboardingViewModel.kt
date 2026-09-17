package com.bagas.pinjam100.presentation.viewmodel.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding
import com.bagas.pinjam100.domain.model.rekening.Rekening
import com.bagas.pinjam100.domain.usecase.customer.SaveCustomerOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@HiltViewModel
class CustomerOnboardingViewModel @Inject constructor(
    private val saveCustomerOnboardingUseCase: SaveCustomerOnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerOnboardingUIState())
    val uiState: StateFlow<CustomerOnboardingUIState> = _uiState.asStateFlow()

    fun updatePersonalData(
        nationalId: String,
        birthDate: String,
        placeOfBirth: String,
        gender: String,
        address: String,
        province: String,
        city: String,
        district: String,
        village: String,
        postalCode: String
    ) {
        _uiState.update {
            it.copy(
                nationalId = nationalId,
                birthDate = birthDate,
                placeOfBirth = placeOfBirth,
                gender = gender,
                address = address,
                province = province,
                city = city,
                district = district,
                village = village,
                postalCode = postalCode
            )
        }
    }

    fun updateEmployment(
        employmentType: String,
        companyName: String,
        position: String,
        monthlyIncome: String,
        startDate: String,
        companyAddress: String,
        companyPhone: String
    ) {
        _uiState.update {
            it.copy(
                employmentType = employmentType,
                companyName = companyName,
                position = position,
                monthlyIncome = monthlyIncome,
                startDate = startDate,
                companyAddress = companyAddress,
                companyPhone = companyPhone
            )
        }
    }

    fun updateRekenings(
        rekenings: List<Rekening>
    ) {
        _uiState.update {
            it.copy(
                rekenings = rekenings
            )
        }
    }

    fun updateNationalId(value: String) {
        _uiState.update {
            it.copy(nationalId = value)
        }
    }

    fun updateBirthDate(value: String) {
        _uiState.update {
            it.copy(birthDate = value)
        }
    }

    fun updatePlaceOfBirth(value: String) {
        _uiState.update {
            it.copy(placeOfBirth = value)
        }
    }

    fun updateGender(value: String) {
        _uiState.update {
            it.copy(gender = value)
        }
    }

    fun updateAddress(value: String) {
        _uiState.update {
            it.copy(address = value)
        }
    }

    fun updateProvince(value: String) {
        _uiState.update {
            it.copy(province = value)
        }
    }

    fun updateCity(value: String) {
        _uiState.update {
            it.copy(city = value)
        }
    }

    fun updateDistrict(value: String) {
        _uiState.update {
            it.copy(district = value)
        }
    }

    fun updateVillage(value: String) {
        _uiState.update {
            it.copy(village = value)
        }
    }

    fun updatePostalCode(value: String) {
        _uiState.update {
            it.copy(postalCode = value)
        }
    }

    fun updateEmploymentType(value: String) {
        _uiState.update {
            it.copy(employmentType = value)
        }
    }

    fun updateCompanyName(value: String) {
        _uiState.update {
            it.copy(companyName = value)
        }
    }

    fun updatePosition(value: String) {
        _uiState.update {
            it.copy(position = value)
        }
    }

    fun updateMonthlyIncome(value: String) {
        _uiState.update {
            it.copy(monthlyIncome = value)
        }
    }

    fun updateStartDate(value: String) {
        _uiState.update {
            it.copy(startDate = value)
        }
    }

    fun updateCompanyAddress(value: String) {
        _uiState.update {
            it.copy(companyAddress = value)
        }
    }

    fun updateCompanyPhone(value: String) {
        _uiState.update {
            it.copy(companyPhone = value)
        }
    }

    fun updateNamaBank(value: String) {
        updateRekening {
            it.copy(namaBank = value)
        }
    }

    fun updateNoRekening(value: String) {
        updateRekening {
            it.copy(noRekening = value)
        }
    }

    fun updateAccountHolder(value: String) {
        updateRekening {
            it.copy(accountHolder = value)
        }
    }

    private fun updateRekening(
        transform: (Rekening) -> Rekening
    ) {
        _uiState.update { state ->
            val rekening = state.rekenings.firstOrNull()
                ?: Rekening(
                    namaBank = "",
                    noRekening = "",
                    accountHolder = ""
                )

            state.copy(
                rekenings = listOf(
                    transform(rekening)
                )
            )
        }
    }

    fun submit(
        customerId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            try {
                val state = uiState.value

                val birthDate = runCatching {
                    LocalDate.parse(
                        state.birthDate,
                        DateTimeFormatter.ofPattern(
                            "d MMMM yyyy",
                            Locale("id", "ID")
                        )
                    ).format(DateTimeFormatter.ISO_LOCAL_DATE)
                }.getOrElse {
                    throw IllegalArgumentException("Format tanggal lahir tidak valid")
                }

                val startDate = runCatching {
                    LocalDate.parse(
                        state.startDate,
                        DateTimeFormatter.ofPattern(
                            "d MMMM yyyy",
                            Locale("id", "ID")
                        )
                    ).format(DateTimeFormatter.ISO_LOCAL_DATE)
                }.getOrElse {
                    throw IllegalArgumentException("Format tanggal mulai bekerja tidak valid")
                }

                val monthlyIncome = state.monthlyIncome
                    .replace(".", "")
                    .replace(",", "")
                    .toLongOrNull()

                val onboarding = CustomerOnboarding(
                    nationalId = state.nationalId.ifBlank { null },
                    birthDate = birthDate,
                    placeOfBirth = state.placeOfBirth.ifBlank { null },
                    gender = state.gender.ifBlank { null },
                    address = state.address.ifBlank { null },
                    province = state.province.ifBlank { null },
                    city = state.city.ifBlank { null },
                    district = state.district.ifBlank { null },
                    village = state.village.ifBlank { null },
                    postalCode = state.postalCode.ifBlank { null },
                    employmentType = state.employmentType.ifBlank { null },
                    companyName = state.companyName.ifBlank { null },
                    position = state.position.ifBlank { null },
                    monthlyIncome = monthlyIncome,
                    startDate = startDate,
                    companyAddress = state.companyAddress.ifBlank { null },
                    companyPhone = state.companyPhone.ifBlank { null },
                    rekenings = state.rekenings
                )

                saveCustomerOnboardingUseCase(
                    customerId = customerId,
                    request = onboarding
                )

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        successMessage = "Data onboarding berhasil disimpan"
                    )
                }

                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = e.message ?: "Gagal menyimpan data"
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

    fun clearSuccess() {
        _uiState.update {
            it.copy(successMessage = null)
        }
    }
}