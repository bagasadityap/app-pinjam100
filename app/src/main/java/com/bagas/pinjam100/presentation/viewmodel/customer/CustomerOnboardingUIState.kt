package com.bagas.pinjam100.presentation.viewmodel.customer

import com.bagas.pinjam100.domain.model.rekening.Rekening

data class CustomerOnboardingUIState(
    val nationalId: String = "",
    val birthDate: String = "",
    val placeOfBirth: String = "",
    val gender: String = "",
    val address: String = "",
    val province: String = "",
    val city: String = "",
    val district: String = "",
    val village: String = "",
    val postalCode: String = "",
    val employmentType: String = "",
    val companyName: String = "",
    val position: String = "",
    val monthlyIncome: String = "",
    val startDate: String = "",
    val companyAddress: String = "",
    val companyPhone: String = "",
    val rekenings: List<Rekening> = emptyList(),
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)