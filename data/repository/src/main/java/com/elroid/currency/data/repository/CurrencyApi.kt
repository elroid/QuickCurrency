package com.elroid.currency.data.repository

import com.elroid.currency.data.remote.CurrencyListResponse
import com.elroid.currency.data.remote.RatesResponse

interface CurrencyApi {
    suspend fun getLatestRates(currencySymbols: List<String>): RatesResponse

    suspend fun getCurrencyList(): CurrencyListResponse
}