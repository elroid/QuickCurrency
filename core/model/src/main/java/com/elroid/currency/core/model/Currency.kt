package com.elroid.currency.core.model

interface Currency {
    val currencyCode: String
    val currencyName: String
    val countryName: String
    val iconUrl: String
}
