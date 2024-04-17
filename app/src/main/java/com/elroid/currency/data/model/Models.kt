package com.elroid.currency.data.model

data class CurrencyValue(
    val amount: Number,
    val currencyCode: String
) : Comparable<CurrencyValue> {
    override fun compareTo(other: CurrencyValue): Int {
        return currencyCode.compareTo(other.currencyCode)
    }
}
