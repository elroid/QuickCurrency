package com.elroid.currency.data.repository.mapper

import com.elroid.currency.core.model.Rates
import com.elroid.currency.data.local.LocalRates
import com.elroid.currency.data.remote.RatesResponse


fun RatesResponse.toRates() = LocalRates(
    key = rates.keys.sorted().joinToString(","),
    ratesMap = rates,
    timestamp = date.toInstant().toEpochMilli()
)