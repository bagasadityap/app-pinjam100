package com.bagas.pinjam100.domain.model.disbursement

import com.bagas.pinjam100.domain.model.rekening.Rekening

data class Disbursement(
    val id: String,
    val loanApplicationId: String,
    val disbursementAmount: Long,
    val createdDate: String,
    val rekening: Rekening
)