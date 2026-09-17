package com.bagas.pinjam100.domain.model.customer

data class Customer(
    val customerNumber: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String? = null
)