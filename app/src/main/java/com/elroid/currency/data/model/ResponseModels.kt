package com.elroid.currency.data.model

import com.elroid.currency.data.deserializer.DateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

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

@Serializable
data class RatesResponse(
    @Serializable(with = DateTimeSerializer::class)
    val date: OffsetDateTime,

    val base: String,

    val rates: Map<String, String>
)