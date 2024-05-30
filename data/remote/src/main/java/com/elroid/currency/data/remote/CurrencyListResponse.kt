package com.elroid.currency.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDescriptorResponse(
    val currencyCode: String,
    val currencyName: String?,
    val countryCode: String?,
    val countryName: String?,
    val icon: String,
)

@Serializable
data class CurrencyListResponse(
    val supportedCurrenciesMap: Map<String, CurrencyDescriptorResponse>
)