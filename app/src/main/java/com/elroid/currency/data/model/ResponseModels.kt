package com.elroid.currency.data.model

import com.elroid.currency.data.deserializer.DateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class RateResult(
    @Serializable(with = DateTimeSerializer::class)
    val date: OffsetDateTime,

    val base: String,

    @SerialName("rates")
    val ratesMap: Map<String, String>
)
