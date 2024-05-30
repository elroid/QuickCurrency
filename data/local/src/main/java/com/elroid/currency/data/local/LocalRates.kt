package com.elroid.currency.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elroid.currency.core.model.CacheData
import com.elroid.currency.core.model.Rates
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "rates")
data class LocalRates(
    @PrimaryKey override val key: String,
    @ColumnInfo(name = "rates_json") override val ratesMap: Map<String, String>,
    @ColumnInfo(name = "timestamp") override val timestamp: Long,
) : Rates, CacheData