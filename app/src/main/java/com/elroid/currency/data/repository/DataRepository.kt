package com.elroid.currency.data.repository

import com.elroid.currency.data.common.addUnique
import com.elroid.currency.data.model.Currency
import com.elroid.currency.data.model.Rates
import com.elroid.currency.data.model.RatesKey
import com.elroid.currency.data.persistence.PreferencesData
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.days

@Single
class DataRepository(
    private val preferencesData: PreferencesData,
    private val currencyListRepository: CurrencyListRepository,
    private val ratesRepository: RatesRepository,
) {
    suspend fun getCurrencyList(): List<Currency> {
        return currencyListRepository.getItem("", MAX_ALL_CURRENCY_CACHE_DURATION).list
    }

    suspend fun getLatestCurrencyRates(): Rates {
        val key = RatesKey(getSelectedCurrencies())
        return ratesRepository.getItem(key, MAX_RATES_CACHE_DURATION)
    }

    fun getBaseCurrency(): String = preferencesData.baseCurrency.value

    suspend fun setBaseCurrency(currencyCode: String) {
        preferencesData.baseCurrency.setValue(currencyCode)
        addSelectedCurrency(currencyCode) // just in case it's not already here
    }

    fun getSelectedCurrencies(): List<String> = preferencesData.selCurrencies.value.currencyCodes

    suspend fun addSelectedCurrency(currencyCode: String) {
        preferencesData.selCurrencies.updateValue {
            copy(currencyCodes = currencyCodes.toMutableList().addUnique(currencyCode))
        }
    }

    suspend fun removeSelectedCurrency(currencyCode: String) {
        preferencesData.selCurrencies.updateValue {
            val newList = currencyCodes.toMutableList()
            newList.remove(currencyCode)
            copy(currencyCodes = newList)
        }
    }

    companion object {
        private val MAX_ALL_CURRENCY_CACHE_DURATION = 7.days
        private val MAX_RATES_CACHE_DURATION = 1.days
    }
}
