//package com.bagas.pinjam100.data.customer.remote
//
//import com.bagas.pinjam100.data.document.remote.DocumentRequest
//import com.bagas.pinjam100.data.rekening.remote.RekeningRequest
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class CustomerRequest(
//    val nationalId: String?,
//    val birthDate: String?,
//    val placeOfBirth: String?,
//    val gender: String?,
//    val address: String?,
//    val province: String?,
//    val city: String?,
//    val district: String?,
//    val village: String?,
//    val postalCode: String?,
//    val employmentType: String?,
//    val companyName: String?,
//    val position: String?,
//    val monthlyIncome: Long?,
//    val startDate: String?,
//    val companyAddress: String?,
//    val companyPhone: String?,
//    val documents: List<DocumentRequest> = emptyList(),
//    val rekenings: List<RekeningRequest> = emptyList()
//)

package com.bagas.pinjam100.data.customer.remote

import kotlinx.serialization.Serializable

@Serializable
data class CustomerRequest(
    val id: String? = null,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String? = null
)