package com.bagas.pinjam100.domain.model.customer

import com.bagas.pinjam100.domain.model.document.Document
import com.bagas.pinjam100.domain.model.rekening.Rekening

data class CustomerDetail(
    val id: String,
    val customerNumber: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val nationalId: String?,
    val verificationStatus: String,
    val createdDate: String,
    val profileCompleted: Boolean,

    val birthDate: String?,
    val placeOfBirth: String?,
    val gender: String?,
    val address: String?,
    val province: String?,
    val city: String?,
    val district: String?,
    val village: String?,
    val postalCode: String?,
    val employmentType: String?,
    val companyName: String?,
    val position: String?,
    val monthlyIncome: Long?,
    val startDate: String?,
    val companyAddress: String?,
    val companyPhone: String?,
    val rekenings: List<Rekening>
)