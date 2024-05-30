package com.elroid.currency.data.repository

import com.elroid.currency.core.model.Rates
import com.elroid.currency.core.model.repository.PrefsRepository
import com.elroid.currency.core.model.repository.RatesRepository
import com.elroid.currency.data.local.LocalRates
import com.elroid.currency.data.local.RatesDao
import com.elroid.currency.data.local.RatesKey
import com.elroid.currency.data.remote.RatesResponse
import com.elroid.currency.data.repository.common.ReadRepository
import com.elroid.currency.data.repository.mapper.toRates
import kotlin.time.Duration.Companion.days

private val MAX_RATES_CACHE_DURATION = 1.days

class RatesRepositoryImpl(
    currencyService: CurrencyApi,
    ratesDao: RatesDao,
    private val prefsRepository: PrefsRepository
) : ReadRepository<RatesKey, RatesResponse, LocalRates>(
    apiFetcher = { currencyService.getLatestRates(it.codes) },
    networkToLocal = { _, response -> response.toRates() },
    dbStream = { ratesDao.streamRates(it.key) },
    dbInsert = { _, value -> ratesDao.insert(value) },
    dbDeleteById = { ratesDao.deleteByKey(it.key) },
    dbDeleteAll = { ratesDao.deleteAll() }
), RatesRepository {
    override suspend fun getLatestCurrencyRates(): Rates {
        val key = RatesKey(prefsRepository.getSelectedCurrencies())
        return getItem(key, MAX_RATES_CACHE_DURATION)
    }
}
