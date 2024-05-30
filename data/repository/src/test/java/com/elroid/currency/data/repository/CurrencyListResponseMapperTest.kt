package com.elroid.currency.data.repository

import com.elroid.currency.core.common.util.nowTs
import com.elroid.currency.data.local.LocalCurrency
import com.elroid.currency.data.local.LocalCurrencyList
import com.elroid.currency.data.remote.CurrencyDescriptorResponse
import com.elroid.currency.data.remote.CurrencyListResponse
import com.elroid.currency.data.repository.mapper.toList
import junit.framework.TestCase
import org.junit.Test
import org.koin.test.KoinTest

class CurrencyListResponseMapperTest : KoinTest {

    private val supportedCurrenciesMap = mapOf<String, CurrencyDescriptorResponse>(
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

    @Test
    fun toList_givenNormalResponseWithNullFields_returnsFilteredList() {
        val now: Long = nowTs()

        val expected = LocalCurrencyList(
            listOf(
                LocalCurrency(
                    "EUR",
                    "Euro",
                    "Andorra",
                    "https://currencyfreaks.com/photos/flags/eur.png"
                ),
                LocalCurrency(
                    "USD",
                    "US Dollar",
                    "American Samoa",
                    "https://currencyfreaks.com/photos/flags/usd.png"
                ),
            ), timestamp = now
        )

        val response = CurrencyListResponse(supportedCurrenciesMap)

        val actual = response.toList(now)
        TestCase.assertEquals(expected, actual)
    }
}