package com.elroid.currency.data.repository

import com.elroid.currency.core.model.Currency
import com.elroid.currency.core.model.repository.CurrencyRepository
import com.elroid.currency.data.local.LocalCurrencyList
import com.elroid.currency.data.local.prefs.PreferencesData
import com.elroid.currency.data.remote.CurrencyListResponse
import com.elroid.currency.data.remote.CurrencyService
import com.elroid.currency.data.repository.common.ReadRepository
import com.elroid.currency.data.repository.mapper.toList
import kotlin.time.Duration.Companion.days

private val MAX_ALL_CURRENCY_CACHE_DURATION = 7.days

class CurrencyRepositoryImpl(
    currencyService: CurrencyService,
    preferencesData: PreferencesData,
) : ReadRepository<String, CurrencyListResponse, LocalCurrencyList>(
    apiFetcher = { currencyService.getCurrencyList() },
    networkToLocal = { _, response -> response.toList() },
    dbStream = { preferencesData.allCurrencies.flow() },
    dbInsert = { _, value -> preferencesData.allCurrencies.setValue(value) },
    dbDeleteById = { preferencesData.allCurrencies.clearValue() },
    dbDeleteAll = { preferencesData.allCurrencies.clearValue() }
), CurrencyRepository {
    override suspend fun getCurrencyList(): List<Currency> = getItem("", MAX_ALL_CURRENCY_CACHE_DURATION).list
}
