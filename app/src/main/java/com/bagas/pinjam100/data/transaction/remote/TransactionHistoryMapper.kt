package com.bagas.pinjam100.data.transaction.remote

import com.bagas.pinjam100.data.transaction.local.TransactionHistoryEntity
import com.bagas.pinjam100.domain.model.transaction.TransactionHistory
import com.bagas.pinjam100.domain.model.transaction.TransactionStatus
import com.bagas.pinjam100.domain.model.transaction.TransactionType
import java.math.BigDecimal

fun TransactionHistoryResponse.toDomain(): TransactionHistory =
    TransactionHistory(
        id = id,
        type = runCatching {
            TransactionType.valueOf(type)
        }.getOrDefault(TransactionType.DISBURSEMENT),
        referenceNumber = referenceNumber,
        amount = BigDecimal.valueOf(amount),
        date = date,
        status = runCatching {
            TransactionStatus.valueOf(status)
        }.getOrDefault(TransactionStatus.PENDING)
    )

fun TransactionHistoryEntity.toDomain(): TransactionHistory =
    TransactionHistory(
        id = id,
        type = runCatching {
            TransactionType.valueOf(type)
        }.getOrDefault(TransactionType.DISBURSEMENT),
        referenceNumber = referenceNumber,
        amount = BigDecimal.valueOf(amount),
        date = date,
        status = runCatching {
            TransactionStatus.valueOf(status)
        }.getOrDefault(TransactionStatus.PENDING)
    )

fun TransactionHistory.toEntity(): TransactionHistoryEntity =
    TransactionHistoryEntity(
        id = id,
        type = type.name,
        referenceNumber = referenceNumber,
        amount = amount.toDouble(),
        date = date,
        status = status.name
    )