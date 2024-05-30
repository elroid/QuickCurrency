package com.elroid.currency.data.local.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.elroid.currency.data.local.LocalCurrencies
import com.elroid.currency.data.local.LocalCurrencyList
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Singleton
import kotlin.reflect.typeOf

@Singleton
class PreferencesData(
    dataStore: DataStore<Preferences>,
    json: Json,
) : BasePreferencesData(dataStore, json) {
    val baseCurrency = createStringPref("baseCurrency", "GBP")
    val selCurrencies = createJsonPref("selCurrencies", typeOf<LocalCurrencies>(), LocalCurrencies("USD", "GBP"))
    val allCurrencies = createJsonPref<LocalCurrencyList>("allCurrencies", typeOf<LocalCurrencyList>())
}