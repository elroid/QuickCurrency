package com.elroid.currency.data.model

import kotlinx.serialization.Serializable

data class CurrencyValue(
    val amount: Number,
    val currencyCode: String
) : Comparable<CurrencyValue> {
    override fun compareTo(other: CurrencyValue): Int {
        return currencyCode.compareTo(other.currencyCode)
    }
}

data class CurrencyDescriptor(
    val currencyCode: String,
    val currencyName: String,
    val countryName: String,
    val iconUrl: String,
)

@Serializable
data class Currencies(
    val currencyCodes: List<String> = emptyList()
) {
    constructor(vararg currencies: String) : this(currencyCodes = currencies.toList())
}