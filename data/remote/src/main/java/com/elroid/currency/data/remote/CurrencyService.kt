package com.elroid.currency.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("rates/latest")
    suspend fun getLatestRates(
        @Query("symbols") currencySymbols: List<String>,
        @Query("apikey") key: String = KEY
    ): RatesResponse

    @GET("supported-currencies")
    suspend fun getCurrencyList(): CurrencyListResponse

    companion object {
        private const val KEY = BuildConfig.CF_API_KEY
    }
}
