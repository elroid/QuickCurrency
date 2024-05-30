package com.elroid.currency.data.repository

import com.elroid.currency.core.common.util.addUnique
import com.elroid.currency.core.model.repository.PrefsRepository
import com.elroid.currency.data.local.prefs.PreferencesData

class PrefsRepositoryImpl(
    private val preferencesData: PreferencesData,
):PrefsRepository {
    override fun getSelectedCurrencies(): List<String> = preferencesData.selCurrencies.value.currencyCodes

    override suspend fun addSelectedCurrency(currencyCode: String) {
        preferencesData.selCurrencies.updateValue {
            copy(currencyCodes = currencyCodes.toMutableList().addUnique(currencyCode))
        }
    }

    override suspend fun removeSelectedCurrency(currencyCode: String) {
        preferencesData.selCurrencies.updateValue {
            val newList = currencyCodes.toMutableList()
            newList.remove(currencyCode)
            copy(currencyCodes = newList)
        }
    }

    override fun getBaseCurrency(): String = preferencesData.baseCurrency.value

    override suspend fun setBaseCurrency(currencyCode: String) {
        preferencesData.baseCurrency.setValue(currencyCode)
        addSelectedCurrency(currencyCode) // just in case it's not already here
    }
}