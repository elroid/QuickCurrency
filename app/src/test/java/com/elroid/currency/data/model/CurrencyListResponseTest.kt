package com.elroid.currency.data.model

import com.elroid.currency.data.dataModule
import com.elroid.currency.data.mappers.toList
import junit.framework.TestCase
import kotlinx.serialization.json.Json
import org.junit.Rule

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class CurrencyListResponseTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create { modules(dataModule) }
    private val json: Json by inject()

    private val jsonResponse = """
        {"supportedCurrenciesMap":{
        "EUR": {
            "currencyCode": "EUR",
            "currencyName": "Euro",
            "countryCode": "AD",
            "countryName": "Andorra",
            "status": "AVAILABLE",
            "availableFrom": "1998-12-31",
            "availableUntil": "2024-04-18",
            "icon": "https://currencyfreaks.com/photos/flags/eur.png"
            },
        "USD": {
            "currencyCode": "USD",
            "currencyName": "US Dollar",
            "countryCode": "AS",
            "countryName": "American Samoa",
            "status": "AVAILABLE",
            "availableFrom": "1984-11-28",
            "availableUntil": "2024-04-18",
            "icon": "https://currencyfreaks.com/photos/flags/usd.png"
            },
        "RLB": {
            "currencyCode": "RLB",
            "currencyName": null,
            "countryCode": null,
            "countryName": null,
            "status": "AVAILABLE",
            "availableFrom": "2023-12-29",
            "availableUntil": "2024-04-18",
            "icon": "https://currencyfreaks.com/photos/flags/rlb.png"
            }
        }}  
        """

    @Test
    fun getCurrencyMap_givenNormalResponseWithNullFields_returnsFilteredList() {
        val expected = listOf(
            CurrencyDescriptor("EUR", "Euro", "Andorra", "https://currencyfreaks.com/photos/flags/eur.png"),
            CurrencyDescriptor("USD", "US Dollar", "American Samoa", "https://currencyfreaks.com/photos/flags/usd.png"),
        )

        val actual = json.decodeFromString<CurrencyListResponse>(jsonResponse).toList()
        TestCase.assertEquals(expected, actual)
    }
}