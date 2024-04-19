package com.elroid.currency.data.repository

import com.elroid.currency.data.mapper.toList
import com.elroid.currency.data.model.CurrencyList
import com.elroid.currency.data.model.CurrencyListResponse
import com.elroid.currency.data.persistence.PreferencesData
import com.elroid.currency.data.repository.common.ReadRepository
import com.elroid.currency.data.service.CurrencyService
import org.koin.core.annotation.Single

@Single
class CurrencyListRepository(
    currencyService: CurrencyService,
    preferencesData: PreferencesData,
) : ReadRepository<String, CurrencyListResponse, CurrencyList>(
    apiFetcher = { currencyService.getCurrencyList() },
    networkToLocal = { _, response -> response.toList() },
    dbStream = { preferencesData.allCurrencies.flow() },
    dbInsert = { _, value -> preferencesData.allCurrencies.setValue(value) },
    dbDeleteById = { preferencesData.allCurrencies.clearValue() },
    dbDeleteAll = { preferencesData.allCurrencies.clearValue() }
)
