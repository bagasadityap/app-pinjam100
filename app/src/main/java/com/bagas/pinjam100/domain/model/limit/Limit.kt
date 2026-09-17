package com.bagas.pinjam100.domain.model.limit

data class Limit(
    val id: String,
    val creditLimit: Long,
    val availableLimit: Long,
    val createdDate: String,
    val updatedDate: String?
)