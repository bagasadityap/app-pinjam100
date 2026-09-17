package com.bagas.pinjam100.data.installment.mapper

import com.bagas.pinjam100.data.installment.local.LoanInstallmentEntity
import com.bagas.pinjam100.data.installment.remote.LoanInstallmentResponse
import com.bagas.pinjam100.domain.model.installment.InstallmentStatus
import com.bagas.pinjam100.domain.model.installment.LoanInstallment
import java.math.BigDecimal

fun LoanInstallmentResponse.toEntity(): LoanInstallmentEntity {
    return LoanInstallmentEntity(
        id = id,
        loanApplicationId = loanApplicationId,
        installmentNumber = installmentNumber,
        installmentSequence = installmentSequence,
        dueDate = dueDate,
        installmentAmount = installmentAmount.toLong(),
        paidAmount = paidAmount.toLong(),
        status = status,
        paidDate = paidDate,
        createdDate = createdDate,
        updatedDate = updatedDate
    )
}

fun LoanInstallmentEntity.toDomain(): LoanInstallment {
    return LoanInstallment(
        id = id,
        loanApplicationId = loanApplicationId,
        installmentNumber = installmentNumber,
        installmentSequence = installmentSequence,
        dueDate = dueDate,
        installmentAmount = BigDecimal.valueOf(installmentAmount),
        paidAmount = BigDecimal.valueOf(paidAmount),
        status = InstallmentStatus.valueOf(status),
        paidDate = paidDate,
        createdDate = createdDate,
        updatedDate = updatedDate
    )
}

fun LoanInstallmentResponse.toDomain(): LoanInstallment {
    return LoanInstallment(
        id = id,
        loanApplicationId = loanApplicationId,
        installmentNumber = installmentNumber,
        installmentSequence = installmentSequence,
        dueDate = dueDate,
        installmentAmount = BigDecimal.valueOf(installmentAmount),
        paidAmount = BigDecimal.valueOf(paidAmount),
        status = InstallmentStatus.valueOf(status),
        paidDate = paidDate,
        createdDate = createdDate,
        updatedDate = updatedDate
    )
}