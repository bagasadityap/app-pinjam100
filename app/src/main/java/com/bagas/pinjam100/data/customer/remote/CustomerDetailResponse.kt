package com.bagas.pinjam100.data.customer.remote

import kotlinx.serialization.Serializable

@Serializable
data class CustomerDetailResponse(
    val id: String,
    val customerNumber: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val nationalId: String?,
    val verificationStatus: String,
    val createdDate: String,
    val profileCompleted: Boolean,

    val detail: CustomerDetailData?,
    val employment: CustomerEmploymentData?,
    val rekening: List<CustomerRekeningData>,

    val documents: List<CustomerDocumentData>,

    val limit: CustomerLimitData?
)

@Serializable
data class CustomerDetailData(
    val id: String,
    val nationalId: String?,
    val birthDate: String?,
    val placeOfBirth: String?,
    val gender: String?,
    val address: String?,
    val province: String?,
    val city: String?,
    val district: String?,
    val village: String?,
    val postalCode: String?
)

@Serializable
data class CustomerEmploymentData(
    val id: String,
    val employmentType: String?,
    val companyName: String?,
    val position: String?,
    val monthlyIncome: Double?,
    val startDate: String?,
    val companyAddress: String?,
    val companyPhone: String?
)

@Serializable
data class CustomerRekeningData(
    val id: String,
    val namaBank: String,
    val noRekening: String,
    val accountHolder: String
)

@Serializable
data class CustomerDocumentData(
    val id: String,
    val customerId: String,
    val fileUrl: String,
    val type: String,
    val verificationStatus: String,
    val verifiedBy: String?
)

@Serializable
data class CustomerLimitData(
    val id: String,
    val customerId: String,
    val creditLimit: Double?,
    val availableLimit: Double?,
    val createdDate: String,
    val updatedDate: String
)