package com.elroid.currency.data.repository.di

import com.elroid.currency.core.model.repository.CurrencyRepository
import com.elroid.currency.core.model.repository.PrefsRepository
import com.elroid.currency.core.model.repository.RatesRepository
import com.elroid.currency.data.remote.CurrencyService
import com.elroid.currency.data.repository.CurrencyApi
import com.elroid.currency.data.repository.CurrencyRepositoryImpl
import com.elroid.currency.data.repository.PrefsRepositoryImpl
import com.elroid.currency.data.repository.RatesRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<CurrencyApi> {
        val service = get<CurrencyService>()
        object : CurrencyApi {
            override suspend fun getCurrencyList() = service.getCurrencyList()
            override suspend fun getLatestRates(currencySymbols: List<String>) = service.getLatestRates(currencySymbols)
        }

    }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get(), get()) }
    single<PrefsRepository> { PrefsRepositoryImpl(get()) }
    single<RatesRepository> { RatesRepositoryImpl(get(), get(), get()) }
}