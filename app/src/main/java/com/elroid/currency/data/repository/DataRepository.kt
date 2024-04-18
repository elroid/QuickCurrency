package com.elroid.currency.data.repository

import com.elroid.currency.data.common.addUnique
import com.elroid.currency.data.mappers.toList
import com.elroid.currency.data.model.CurrencyDescriptor
import com.elroid.currency.data.model.RatesResponse
import com.elroid.currency.data.persistence.PreferencesData
import com.elroid.currency.data.service.CurrencyService
import org.koin.core.annotation.Factory

@Factory
class DataRepository(
    private val currencyService: CurrencyService,
    private val preferencesData: PreferencesData
) {
    suspend fun getCurrencyList(): List<CurrencyDescriptor> = currencyService.getCurrencyList().toList()

    suspend fun getLatestCurrencyRates(): RatesResponse = currencyService.getLatestRates(getSelectedCurrencies())

    fun getBaseCurrency(): String = preferencesData.baseCurrency.value

    suspend fun setBaseCurrency(currencyCode: String) {
        preferencesData.baseCurrency.setValue(currencyCode)
        addSelectedCurrency(currencyCode) // just in case it's not already here
    }

    fun getSelectedCurrencies(): List<String> = preferencesData.currencies.value.currencyCodes

    suspend fun addSelectedCurrency(currencyCode: String) {
        preferencesData.currencies.updateValue {
            copy(currencyCodes = currencyCodes.toMutableList().addUnique(currencyCode))
        }
    }

    suspend fun removeSelectedCurrency(currencyCode: String) {
        preferencesData.currencies.updateValue {
            val newList = currencyCodes.toMutableList()
            newList.remove(currencyCode)
            copy(currencyCodes = newList)
        }
    }
}
