package com.elroid.currency.data.local

import kotlinx.serialization.Serializable

@Serializable
data class LocalCurrencies(
    val currencyCodes: List<String> = emptyList()
) {
    constructor(vararg currencies: String) : this(currencyCodes = currencies.toList())
}