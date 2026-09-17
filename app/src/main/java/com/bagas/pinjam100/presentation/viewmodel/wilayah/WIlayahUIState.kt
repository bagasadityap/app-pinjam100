package com.bagas.pinjam100.presentation.viewmodel.wilayah

import com.bagas.pinjam100.domain.model.wilayah.Wilayah

data class WilayahUIState(
    val provinces: List<Wilayah> = emptyList(),
    val regencies: List<Wilayah> = emptyList(),
    val districts: List<Wilayah> = emptyList(),
    val villages: List<Wilayah> = emptyList(),

    val selectedProvince: Wilayah? = null,
    val selectedRegency: Wilayah? = null,
    val selectedDistrict: Wilayah? = null,
    val selectedVillage: Wilayah? = null,

    val isLoadingProvinces: Boolean = false,
    val isLoadingRegencies: Boolean = false,
    val isLoadingDistricts: Boolean = false,
    val isLoadingVillages: Boolean = false,

    val errorMessage: String? = null
)