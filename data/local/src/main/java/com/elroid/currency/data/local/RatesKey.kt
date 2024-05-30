package com.elroid.currency.data.local

class RatesKey(val codes: List<String>) {
    val key: String get() = codes.sorted().joinToString(",")
}