package com.elroid.currency.data.mappers

import com.elroid.currency.data.model.CurrencyDescriptor
import com.elroid.currency.data.model.CurrencyListResponse

fun CurrencyListResponse.toList(): List<CurrencyDescriptor> = currencyMap.values
    // filter out currencies with incomplete data
    .filter { it.currencyName.isNullOrBlank().not() && it.countryName.isNullOrBlank().not() }

    // filter out crypto (not interested in these for now)
    .filter { it.countryCode != "Crypto" }

    .map {
        CurrencyDescriptor(
            it.currencyCode,
            it.currencyName!!,
            it.countryName!!,
            it.iconUrl,
        )
    }.sortedBy { it.currencyCode }