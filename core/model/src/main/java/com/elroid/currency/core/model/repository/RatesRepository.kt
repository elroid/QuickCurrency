package com.elroid.currency.core.model.repository

import com.elroid.currency.core.model.Rates

interface RatesRepository {
    suspend fun getLatestCurrencyRates(): Rates
}