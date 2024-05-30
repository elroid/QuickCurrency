package com.elroid.currency.data.repository.mapper

import com.elroid.currency.core.common.util.nowTs
import com.elroid.currency.core.model.Currency
import com.elroid.currency.core.model.CurrencyList
import com.elroid.currency.data.local.LocalCurrency
import com.elroid.currency.data.local.LocalCurrencyList
import com.elroid.currency.data.remote.CurrencyListResponse


fun CurrencyListResponse.toList(now: Long = nowTs()): LocalCurrencyList = supportedCurrenciesMap.values
    // filter out currencies with incomplete data
    .filter { it.currencyName.isNullOrBlank().not() && it.countryName.isNullOrBlank().not() }

    // filter out crypto (not interested in these for now)
    .filter { it.countryCode != "Crypto" }

    .map {
        LocalCurrency(
            it.currencyCode,
            it.currencyName!!,
            it.countryName!!,
            it.icon,
        )
    }.sortedBy { it.currencyCode }

    // convert to LocalCurrencyList
    .let { LocalCurrencyList(it, now) }