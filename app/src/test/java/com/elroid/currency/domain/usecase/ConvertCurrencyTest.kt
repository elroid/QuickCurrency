package com.elroid.currency.domain.usecase

import com.elroid.currency.data.dataModule
import com.elroid.currency.data.model.CurrencyValue
import com.elroid.currency.data.model.RatesResponse
import com.elroid.currency.data.repository.DataRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class ConvertCurrencyTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create { modules(dataModule) }
    private val json: Json by inject()

    private val ratesResponse: RatesResponse by lazy {
        val jsonString = """
        {
          "date":"2024-04-16 00:00:00+00",
          "base":"USD",
          "rates":{
            "JPY":"154.21275",
            "EUR":"0.9411246716283831",
            "GBP":"0.803440826169263",
            "COP":"3890.066705818813",
            "USD":"1.0"
          }
        }
        """
        json.decodeFromString<RatesResponse>(jsonString)
    }

    private fun createConverter(selectedCurrencies: List<String>): ConvertCurrency {
        val mockDataRepository = mockk<DataRepository>()
        coEvery { mockDataRepository.getLatestCurrencyRates() } returns ratesResponse
        coEvery { mockDataRepository.getSelectedCurrencies() } returns selectedCurrencies
        return ConvertCurrency(mockDataRepository)
    }

    @Test
    fun invoke_givenAmountInBaseCurrency_returnsExpected() = runTest {
        val converter = createConverter(listOf("EUR"))
        val fromValue = CurrencyValue(1234.56, "USD")
        val expectedValue = mapOf("EUR" to CurrencyValue(1161.8748746055367, "EUR"))
        val actual = converter(fromValue)
        assertEquals(expectedValue, actual)
    }

    @Test
    fun invoke_givenAmountInNonBaseCurrency_returnsExpected() = runTest {
        val converter = createConverter(listOf("EUR"))
        val fromValue = CurrencyValue(1234.56, "GBP")
        val expectedValue = mapOf("EUR" to CurrencyValue(1446.1237676273638, "EUR"))
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