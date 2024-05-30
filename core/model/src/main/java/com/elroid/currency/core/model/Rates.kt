package com.elroid.currency.core.model

interface Rates : CacheData {
    val key: String
    val ratesMap: Map<String, String>
}
