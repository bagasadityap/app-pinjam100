package com.bagas.pinjam100.data.limit.mapper

import com.bagas.pinjam100.data.limit.local.LimitEntity
import com.bagas.pinjam100.data.limit.remote.LimitResponse
import com.bagas.pinjam100.domain.model.limit.Limit

fun LimitResponse.toDomain() = Limit(
    id = id,
    customerId = customerId,
    creditLimit = creditLimit.toLong(),
    availableLimit = availableLimit.toLong(),
    createdDate = createdDate,
    updatedDate = updatedDate
)

fun LimitResponse.toEntity() = LimitEntity(
    customerId = customerId,
    id = id,
    creditLimit = creditLimit,
    availableLimit = availableLimit,
    createdDate = createdDate,
    updatedDate = updatedDate
)

fun LimitEntity.toDomain() = Limit(
    id = id,
    customerId = customerId,
    creditLimit = creditLimit.toLong(),
    availableLimit = availableLimit.toLong(),
    createdDate = createdDate,
    updatedDate = updatedDate
)