package com.elroid.currency.data.remote

import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class RatesResponse(
    @Serializable(with = DateTimeSerializer::class)
    val date: OffsetDateTime,

    val base: String,

    val rates: Map<String, String>
)