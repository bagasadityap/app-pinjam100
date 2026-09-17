package com.bagas.pinjam100.domain.usecase.wilayah

import com.bagas.pinjam100.domain.repository.WilayahRepository
import javax.inject.Inject

class GetDistrictsUseCase @Inject constructor(
    private val repository: WilayahRepository
) {
    suspend operator fun invoke(
        regencyCode: String
    ) = repository.getDistricts(regencyCode)
}