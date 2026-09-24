package com.bagas.pinjam100.data.customer.remote

import com.bagas.pinjam100.data.rekening.remote.toRequest
import com.bagas.pinjam100.domain.model.customer.Customer
import com.bagas.pinjam100.domain.model.customer.CustomerDetail
import com.bagas.pinjam100.domain.model.customer.CustomerOnboarding
import com.bagas.pinjam100.domain.model.rekening.Rekening

fun CustomerResponse.toDomain() = Customer(
    customerNumber = customerNumber,
    fullName = fullName,
    email = email,
    phoneNumber = phoneNumber
)

fun CustomerOnboarding.toRequest(): CustomerOnboardingRequest {
    return CustomerOnboardingRequest(
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

fun CustomerDetailResponse.toDomain(): CustomerDetail {
    return CustomerDetail(
        birthDate = detail?.birthDate,
        placeOfBirth = detail?.placeOfBirth,
        gender = detail?.gender,
        address = detail?.address,
        province = detail?.province,
        city = detail?.city,
        district = detail?.district,
        village = detail?.village,
        postalCode = detail?.postalCode,

        employmentType = employment?.employmentType,
        companyName = employment?.companyName,
        position = employment?.position,
        monthlyIncome = employment?.monthlyIncome?.toLong(),
        startDate = employment?.startDate,
        companyAddress = employment?.companyAddress,
        companyPhone = employment?.companyPhone,

        rekenings = rekening.map {
            Rekening(
                namaBank = it.namaBank,
                noRekening = it.noRekening,
                accountHolder = it.accountHolder
            )
        }
    )
}