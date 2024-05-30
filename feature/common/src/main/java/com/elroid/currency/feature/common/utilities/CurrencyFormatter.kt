package com.elroid.currency.feature.common.utilities

import android.icu.text.NumberFormat
import android.icu.util.Currency
import com.elroid.currency.core.model.CurrencyValue
import java.util.*

fun CurrencyValue.format(): String {
    val currency = Currency.getInstance(currencyCode)
    val formatter = NumberFormat.getCurrencyInstance()
    formatter.maximumFractionDigits = currency.defaultFractionDigits
    formatter.currency = currency
    return formatter.format(amount)
}

val CurrencyValue.symbol: String
    get() {
        val currency = Currency.getInstance(currencyCode)
        val symbol = currency.getSymbol(Locale.getDefault())
        return symbol.takeIf { !symbol.equals(currencyCode) } ?: ""
    }