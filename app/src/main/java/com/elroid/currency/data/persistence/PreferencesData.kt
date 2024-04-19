package com.elroid.currency.data.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.elroid.currency.data.model.Currencies
import com.elroid.currency.data.model.CurrencyList
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import kotlin.reflect.typeOf

@Factory
class PreferencesData(
    dataStore: DataStore<Preferences>,
    json: Json,
) : BasePreferencesData(dataStore, json) {
    val baseCurrency = createStringPref("baseCurrency", "GBP")
    val selCurrencies = createJsonPref("selCurrencies", typeOf<Currencies>(), Currencies("USD", "GBP"))
    val allCurrencies = createJsonPref<CurrencyList>("allCurrencies", typeOf<CurrencyList>())
}