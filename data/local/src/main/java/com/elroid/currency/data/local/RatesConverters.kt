package com.elroid.currency.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RatesConverters {
    @TypeConverter
    fun ratesMapToJson(map: Map<String, String>): String = Json.encodeToString(map)

    @TypeConverter
    fun jsonToRatesMap(json: String): Map<String, String> = Json.decodeFromString(json)
}