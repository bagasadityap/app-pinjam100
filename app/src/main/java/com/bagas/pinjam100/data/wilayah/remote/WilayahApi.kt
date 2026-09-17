package com.bagas.pinjam100.data.wilayah.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface WilayahApi {

    @GET("provinces.json")
    suspend fun getProvinces(): WilayahResponse

    @GET("regencies/{provinceCode}.json")
    suspend fun getRegencies(
        @Path("provinceCode") provinceCode: String
    ): WilayahResponse

    @GET("districts/{regencyCode}.json")
    suspend fun getDistricts(
        @Path("regencyCode") regencyCode: String
    ): WilayahResponse

    @GET("villages/{districtCode}.json")
    suspend fun getVillages(
        @Path("districtCode") districtCode: String
    ): WilayahResponse
}