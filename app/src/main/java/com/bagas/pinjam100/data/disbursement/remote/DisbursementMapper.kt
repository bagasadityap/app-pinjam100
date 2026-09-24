package com.bagas.pinjam100.data.disbursement.remote

import com.bagas.pinjam100.data.rekening.remote.toDomain
import com.bagas.pinjam100.domain.model.disbursement.Disbursement

fun DisbursementResponse.toDomain(): Disbursement {
    return Disbursement(
        id = id,
        loanApplicationId = loanApplicationId,
        disbursementAmount = disbursementAmount.toLong(),
        createdDate = createdDate,
        rekening = rekening.toDomain()
    )
}