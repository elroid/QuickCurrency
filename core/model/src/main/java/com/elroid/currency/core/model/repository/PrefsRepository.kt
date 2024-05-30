package com.elroid.currency.core.model.repository

interface PrefsRepository {
    fun getSelectedCurrencies(): List<String>
    suspend fun addSelectedCurrency(currencyCode: String)
    suspend fun removeSelectedCurrency(currencyCode: String)

    fun getBaseCurrency(): String
    suspend fun setBaseCurrency(currencyCode: String)
}