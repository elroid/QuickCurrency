package com.elroid.currency.core.model

data class Currencies(
    val currencyCodes: List<String> = emptyList()
) {
    constructor(vararg currencies: String) : this(currencyCodes = currencies.toList())
}