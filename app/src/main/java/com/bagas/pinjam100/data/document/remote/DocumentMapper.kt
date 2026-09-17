package com.bagas.pinjam100.data.document.remote

import com.bagas.pinjam100.domain.model.document.Document

fun DocumentResponse.toDomain() = Document(
    id = id,
    fileUrl = fileUrl,
    type = type,
    verificationStatus = verificationStatus,
    verifiedBy = verifiedBy,
    customerId = customerId,
)

fun Document.toRequest() = DocumentRequest(
    customerId = customerId,
    type = type
)