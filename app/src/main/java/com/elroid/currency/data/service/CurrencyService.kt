package com.elroid.currency.data.service

import com.elroid.currency.BuildConfig
import com.elroid.currency.data.model.CurrencyListResponse
import com.elroid.currency.data.model.RatesResponse
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
