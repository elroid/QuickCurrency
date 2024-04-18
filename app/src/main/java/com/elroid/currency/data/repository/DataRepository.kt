package com.elroid.currency.data.repository

import com.elroid.currency.data.mappers.toList
import com.elroid.currency.data.model.CurrencyDescriptor
import com.elroid.currency.data.model.RatesResponse
import com.elroid.currency.data.service.CurrencyService
import org.koin.core.annotation.Factory

@Factory
class DataRepository(
    private val currencyService: CurrencyService
) {
    suspend fun getCurrencyList(): List<CurrencyDescriptor> = currencyService.getCurrencyList().toList()

    suspend fun getLatestCurrencyRates(): RatesResponse = currencyService.getLatestRates(getSelectedCurrencies())

    fun getSelectedCurrencies() = listOf("GBP", "USD", "EUR", "JPY", "AUD")
}