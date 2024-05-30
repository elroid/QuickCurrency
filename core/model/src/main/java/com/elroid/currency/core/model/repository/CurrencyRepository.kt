package com.elroid.currency.core.model.repository

import com.elroid.currency.core.model.Currency

interface CurrencyRepository {
    suspend fun getCurrencyList(): List<Currency>
}