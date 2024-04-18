package com.elroid.currency.data.model

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