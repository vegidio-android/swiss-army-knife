package io.vinicius.sak.network

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesService
{
    companion object {
        internal const val BASE_URL = "https://restcountries.eu/rest/v2/"
    }

    @GET("alpha/{countryCode}")
    fun getCountryByCode(
        @Path("countryCode") countryCode: String
    ): Flow<Country>
}