package com.bagas.pinjam100.domain.usecase.wilayah

import com.bagas.pinjam100.domain.repository.WilayahRepository
import javax.inject.Inject

class GetProvincesUseCase @Inject constructor(
    private val repository: WilayahRepository
) {
    suspend operator fun invoke() =
        repository.getProvinces()
}