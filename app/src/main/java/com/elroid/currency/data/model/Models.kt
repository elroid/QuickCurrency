package com.elroid.currency.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

interface CacheData {
    val timestamp: Long
}

@Serializable
data class Currencies(
    val currencyCodes: List<String> = emptyList()
) {
    constructor(vararg currencies: String) : this(currencyCodes = currencies.toList())
}

@Serializable
data class Currency(
    val currencyCode: String,
    val currencyName: String,
    val countryName: String,
    val iconUrl: String,
)

@Serializable
data class CurrencyList(
    val list: List<Currency>,
    override val timestamp: Long
) : CacheData

data class CurrencyValue(
    val amount: Number,
    val currencyCode: String
)

@Serializable
@Entity(tableName = "rates")
data class Rates(
    @PrimaryKey val key: String,
    @ColumnInfo(name = "rates_json") val ratesMap: Map<String, String>,
    @ColumnInfo(name = "timestamp") override val timestamp: Long,
) : CacheData

class RatesKey(val codes: List<String>) {
    val key: String get() = codes.sorted().joinToString(",")
}
