package com.bagas.pinjam100.data.loanapplication.remote

import com.bagas.pinjam100.data.customer.remote.toDomain
import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication

fun LoanApplicationResponse.toDomain() = LoanApplication(
    id = id,
    applicationId = applicationId,
    customer = customer.toDomain(),
    loanAmount = loanAmount.toBigDecimal(),
    tenorMonths = tenorMonths,
    interestRate = interestRate.toBigDecimal(),
    purpose = purpose,
    status = status,
    createdDate = createdDate
)