package com.bagas.pinjam100.presentation.viewmodel.wilayah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas.pinjam100.domain.model.wilayah.Wilayah
import com.bagas.pinjam100.domain.usecase.wilayah.GetDistrictsUseCase
import com.bagas.pinjam100.domain.usecase.wilayah.GetProvincesUseCase
import com.bagas.pinjam100.domain.usecase.wilayah.GetRegenciesUseCase
import com.bagas.pinjam100.domain.usecase.wilayah.GetVillagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WilayahViewModel @Inject constructor(
    private val getProvinces: GetProvincesUseCase,
    private val getRegencies: GetRegenciesUseCase,
    private val getDistricts: GetDistrictsUseCase,
    private val getVillages: GetVillagesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WilayahUIState())
    val uiState: StateFlow<WilayahUIState> = _uiState.asStateFlow()

    init {
        loadProvinces()
    }

    fun loadProvinces() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingProvinces = true,
                errorMessage = null
            )

            getProvinces()
                .onSuccess { provinces ->
                    _uiState.value = _uiState.value.copy(
                        provinces = provinces,
                        isLoadingProvinces = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingProvinces = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun selectProvince(province: Wilayah) {
        _uiState.value = _uiState.value.copy(
            selectedProvince = province,
            selectedRegency = null,
            selectedDistrict = null,
            selectedVillage = null,
            regencies = emptyList(),
            districts = emptyList(),
            villages = emptyList()
        )

        loadRegencies(province.code)
    }

    private fun loadRegencies(provinceCode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingRegencies = true,
                errorMessage = null
            )

            getRegencies(provinceCode)
                .onSuccess { regencies ->
                    _uiState.value = _uiState.value.copy(
                        regencies = regencies,
                        isLoadingRegencies = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingRegencies = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun selectRegency(regency: Wilayah) {
        _uiState.value = _uiState.value.copy(
            selectedRegency = regency,
            selectedDistrict = null,
            selectedVillage = null,
            districts = emptyList(),
            villages = emptyList()
        )

        loadDistricts(regency.code)
    }

    private fun loadDistricts(regencyCode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingDistricts = true,
                errorMessage = null
            )

            getDistricts(regencyCode)
                .onSuccess { districts ->
                    _uiState.value = _uiState.value.copy(
                        districts = districts,
                        isLoadingDistricts = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingDistricts = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun selectDistrict(district: Wilayah) {
        _uiState.value = _uiState.value.copy(
            selectedDistrict = district,
            selectedVillage = null,
            villages = emptyList()
        )

        loadVillages(district.code)
    }

    private fun loadVillages(districtCode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingVillages = true,
                errorMessage = null
            )

            getVillages(districtCode)
                .onSuccess { villages ->
                    _uiState.value = _uiState.value.copy(
                        villages = villages,
                        isLoadingVillages = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingVillages = false,
                        errorMessage = error.message
                    )
                }
        }
    }

    fun selectVillage(village: Wilayah) {
        _uiState.value = _uiState.value.copy(
            selectedVillage = village
        )
    }
}