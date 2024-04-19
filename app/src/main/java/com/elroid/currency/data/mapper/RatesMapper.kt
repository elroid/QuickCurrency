package com.elroid.currency.data.mapper

import com.elroid.currency.data.model.Rates
import com.elroid.currency.data.model.RatesResponse

fun RatesResponse.toRates() = Rates(
    key = rates.keys.sorted().joinToString(","),
    ratesMap = rates,
    timestamp = date.toInstant().toEpochMilli()
)