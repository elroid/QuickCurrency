package com.elroid.currency.data.local

import com.elroid.currency.core.model.CacheData
import com.elroid.currency.core.model.Currency
import com.elroid.currency.core.model.CurrencyList
import kotlinx.serialization.Serializable

@Serializable
data class LocalCurrency(
    override val currencyCode: String,
    override val currencyName: String,
    override val countryName: String,
    override val iconUrl: String,
): Currency

@Serializable
data class LocalCurrencyList(
    override val list: List<LocalCurrency>,
    override val timestamp: Long
) : CacheData, CurrencyList