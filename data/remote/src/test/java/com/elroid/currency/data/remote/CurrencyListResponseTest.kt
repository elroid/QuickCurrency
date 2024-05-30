package com.elroid.currency.data.remote

import com.elroid.currency.data.remote.di.remoteModule
import junit.framework.TestCase
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class CurrencyListResponseTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create { modules(remoteModule) }
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
    fun getCurrencyMap_givenNormalResponseWithNullFields_returnsObjectsWithNullFields() {
        val supportedCurrenciesMap = mapOf(
            "EUR" to CurrencyDescriptorResponse(
                "EUR",
                "Euro",
                "AD",
                "Andorra",
                "https://currencyfreaks.com/photos/flags/eur.png"
            ),
            "USD" to CurrencyDescriptorResponse(
                "USD",
                "US Dollar",
                "AS",
                "American Samoa",
                "https://currencyfreaks.com/photos/flags/usd.png"
            ),
            "RLB" to CurrencyDescriptorResponse(
                "RLB",
                null,
                null,
                null,
                "https://currencyfreaks.com/photos/flags/rlb.png"
            )
        )
        val expected = CurrencyListResponse(supportedCurrenciesMap)

        val actual = json.decodeFromString<CurrencyListResponse>(jsonResponse)
        TestCase.assertEquals(expected, actual)
    }
}