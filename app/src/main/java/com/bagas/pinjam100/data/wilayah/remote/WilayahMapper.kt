package com.bagas.pinjam100.data.wilayah.remote

import com.bagas.pinjam100.domain.model.wilayah.Wilayah

fun WilayahDto.toDomain(): Wilayah {
    return Wilayah(
        code = code,
        name = name
    )
}