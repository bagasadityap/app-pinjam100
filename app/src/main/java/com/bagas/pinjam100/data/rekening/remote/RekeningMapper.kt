package com.bagas.pinjam100.data.rekening.remote

import com.bagas.pinjam100.domain.model.rekening.Rekening

fun RekeningResponse.toDomain() = Rekening(
    namaBank = namaBank,
    noRekening = noRekening,
    accountHolder = accountHolder
)

fun Rekening.toRequest() = RekeningRequest(
    namaBank = namaBank,
    noRekening = noRekening,
    accountHolder = accountHolder
)