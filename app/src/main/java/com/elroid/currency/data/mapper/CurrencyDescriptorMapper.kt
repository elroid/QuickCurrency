package com.elroid.currency.data.mapper

import com.elroid.currency.data.common.nowTs
import com.elroid.currency.data.model.Currency
import com.elroid.currency.data.model.CurrencyList
import com.elroid.currency.data.model.CurrencyListResponse

fun CurrencyListResponse.toList(now: Long = nowTs()): CurrencyList = supportedCurrenciesMap.values
    // filter out currencies with incomplete data
    .filter { it.currencyName.isNullOrBlank().not() && it.countryName.isNullOrBlank().not() }

    // filter out crypto (not interested in these for now)
    .filter { it.countryCode != "Crypto" }

    .map {
        Currency(
            it.currencyCode,
            it.currencyName!!,
            it.countryName!!,
            it.icon,
        )
    }.sortedBy { it.currencyCode }

    // convert to CurrencyDescriptorList
    .let { CurrencyList(it, now) }