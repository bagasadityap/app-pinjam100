package com.bagas.pinjam100.data.customer.remote

import com.bagas.pinjam100.data.document.remote.toRequest
import com.bagas.pinjam100.data.rekening.remote.toRequest
import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerDetail
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding

fun CustomerResponse.toDomain() = Customer(
    customerNumber = customerNumber,
    fullName = fullName,
    email = email,
    phoneNumber = phoneNumber
)

fun CustomerOnboarding.toRequest(): CustomerOnboardingRequest {
    return CustomerOnboardingRequest(
        nationalId = nationalId,
        birthDate = birthDate,
        placeOfBirth = placeOfBirth,
        gender = gender,
        address = address,
        province = province,
        city = city,
        district = district,
        village = village,
        postalCode = postalCode,
        employmentType = employmentType,
        companyName = companyName,
        position = position,
        monthlyIncome = monthlyIncome,
        startDate = startDate,
        companyAddress = companyAddress,
        companyPhone = companyPhone,
        rekenings = rekenings.map { it.toRequest() }
    )
}