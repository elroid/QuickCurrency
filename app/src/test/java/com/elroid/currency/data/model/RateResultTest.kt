package com.elroid.currency.data.model

import com.elroid.currency.data.dataModule
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.time.OffsetDateTime
import java.time.ZoneOffset

class RateResultTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create { modules(dataModule) }
    private val json: Json by inject()

    private val zeroTimeTestJson = """
        {
            "date": "2024-04-16 00:00:00+00",
            "base": "USD",
            "rates": {
                "EUR": "0.9411246716283831",
                "GBP": "0.803440826169263",
                "COP": "3890.066705818813",
                "USD": "1.0"
            }
        }
        """

    @Test
    fun deserialize_givenResultWithZeroTime_returnsExpected() {
        val expected = RateResult(
            date = OffsetDateTime.of(2024, 4, 16, 0, 0, 0, 0, ZoneOffset.UTC),
            base = "USD",
            ratesMap = mapOf(
                "EUR" to "0.9411246716283831",
                "GBP" to "0.803440826169263",
                "COP" to "3890.066705818813",
                "USD" to "1.0",
            )
        )
        val actual = json.decodeFromString<RateResult>(zeroTimeTestJson)
        assertEquals(expected, actual)
    }
}