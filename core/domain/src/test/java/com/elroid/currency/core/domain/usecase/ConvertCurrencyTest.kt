package com.elroid.currency.core.domain.usecase

import com.elroid.currency.core.model.CurrencyValue
import com.elroid.currency.core.model.Rates
import com.elroid.currency.core.model.repository.PrefsRepository
import com.elroid.currency.core.model.repository.RatesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertCurrencyTest {

    private val rates: Rates by lazy {
        object : Rates {
            override val key: String = "Unused"
            override val ratesMap: Map<String, String> = mapOf(
                "JPY" to "154.21275",
                "EUR" to "0.9411246716283831",
                "GBP" to "0.803440826169263",
                "COP" to "3890.066705818813",
                "USD" to "1.0"
            )
            override val timestamp: Long = 1713225600000 // 2024-04-16 00:00:00+00
        }
    }

    private fun createConverter(selectedCurrencies: List<String>): ConvertCurrency {
        val mockRatesRepository = mockk<RatesRepository>()
        val mockPrefsRepository = mockk<PrefsRepository>()
        coEvery { mockRatesRepository.getLatestCurrencyRates() } returns rates
        coEvery { mockPrefsRepository.getSelectedCurrencies() } returns selectedCurrencies
        return ConvertCurrency(mockRatesRepository, mockPrefsRepository)
    }

    @Test
    fun invoke_givenAmountInBaseCurrency_returnsExpected() = runTest {
        val converter = createConverter(listOf("EUR"))
        val fromValue = CurrencyValue(1234.56, "USD")
        val expectedValue = ConversionResult(
            mapOf("EUR" to CurrencyValue(1161.8748746055367, "EUR")),
            timestamp = 1713225600000L
        )
        val actual = converter(fromValue)
        assertEquals(expectedValue, actual)
    }

    @Test
    fun invoke_givenAmountInNonBaseCurrency_returnsExpected() = runTest {
        val converter = createConverter(listOf("EUR"))
        val fromValue = CurrencyValue(1234.56, "GBP")
        val expectedValue = ConversionResult(
            mapOf("EUR" to CurrencyValue(1446.1237676273638, "EUR")),
            1713225600000L
        )
        val actual = converter(fromValue)
        assertEquals(expectedValue, actual)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invoke_givenFromAmountNotInRatesResult_throwsError() = runTest {
        val converter = createConverter(listOf("MXN"))
        val fromValue = CurrencyValue(1234.56, "GBP")
        converter.invoke(fromValue)
    }

    @Test(expected = IllegalArgumentException::class)
    fun invoke_givenToAmountNotInRatesResult_throwsError() = runTest {
        val converter = createConverter(listOf("EUR"))
        val fromValue = CurrencyValue(1234.56, "MXN")
        converter.invoke(fromValue)
    }
}