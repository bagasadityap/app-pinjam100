package com.bagas.pinjam100.data.limit.remote

import com.bagas.pinjam100.domain.model.limit.Limit

fun LimitResponse.toDomain() = Limit(
    id = id,
    creditLimit = creditLimit.toLong(),
    availableLimit = availableLimit.toLong(),
    createdDate = createdDate,
    updatedDate = updatedDate
)