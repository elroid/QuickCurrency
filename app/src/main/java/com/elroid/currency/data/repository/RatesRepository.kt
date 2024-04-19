package com.elroid.currency.data.repository

import com.elroid.currency.data.mapper.toRates
import com.elroid.currency.data.model.Rates
import com.elroid.currency.data.model.RatesKey
import com.elroid.currency.data.model.RatesResponse
import com.elroid.currency.data.persistence.RatesDao
import com.elroid.currency.data.repository.common.ReadRepository
import com.elroid.currency.data.service.CurrencyService
import org.koin.core.annotation.Single

@Single
class RatesRepository(
    currencyService: CurrencyService,
    ratesDao: RatesDao,
) : ReadRepository<RatesKey, RatesResponse, Rates>(
    apiFetcher = { currencyService.getLatestRates(it.codes) },
    networkToLocal = { _, response -> response.toRates() },
    dbStream = { ratesDao.streamRates(it.key) },
    dbInsert = { _, value -> ratesDao.insert(value) },
    dbDeleteById = { ratesDao.deleteByKey(it.key) },
    dbDeleteAll = { ratesDao.deleteAll() }
)
