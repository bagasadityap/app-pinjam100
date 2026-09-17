package com.bagas.pinjam100.data.customer.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class CustomerEntity(
    @PrimaryKey
    val id: String,
    val customerNumber: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val verificationStatus: String,
    val createdDate: String,
    val nationalId: String?,
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
    val creditLimit: Long?,
    val limitCreatedDate: String?,
    val limitUpdatedDate: String?
)