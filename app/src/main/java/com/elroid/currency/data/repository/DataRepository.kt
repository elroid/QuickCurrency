package com.elroid.currency.data.repository

import com.elroid.currency.data.model.RateResult
import com.elroid.currency.data.service.CurrencyService
import org.koin.core.annotation.Factory

@Factory
class DataRepository(
    private val currencyService: CurrencyService
) {
    suspend fun getLatestCurrencyRates(): RateResult {
        return currencyService.getLatestRates(getSelectedCurrencies())
    }

    fun getSelectedCurrencies() = listOf("GBP", "USD", "EUR", "JPY", "COP")
}