package com.bagas.pinjam100.domain.usecase.wilayah

import com.bagas.pinjam100.domain.repository.WilayahRepository
import javax.inject.Inject

class GetVillagesUseCase @Inject constructor(
    private val repository: WilayahRepository
) {
    suspend operator fun invoke(
        districtCode: String
    ) = repository.getVillages(districtCode)
}