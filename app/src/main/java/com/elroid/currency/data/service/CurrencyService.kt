package com.elroid.currency.data.service

import com.elroid.currency.BuildConfig
import com.elroid.currency.data.model.RateResult
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("rates/latest")
    suspend fun getLatestRates(
        @Query("symbols") currencySymbols: List<String>,
        @Query("apikey") key: String = KEY
    ): RateResult

    companion object {
        private const val KEY = BuildConfig.CF_API_KEY
    }
}
