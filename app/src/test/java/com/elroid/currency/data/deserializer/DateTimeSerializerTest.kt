package com.elroid.currency.data.deserializer

import com.elroid.currency.data.dataModule
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.time.OffsetDateTime
import java.time.ZoneOffset

class DateTimeSerializerTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create { modules(dataModule) }
    private val json: Json by inject()

    @Test
    fun serializeAndDeserialize_shouldBeReciprocal() {
        val originalDateTime = OffsetDateTime.of(2024, 4, 16, 9, 53, 15, 0, ZoneOffset.UTC)
        val serializedDateTime = json.encodeToString(originalDateTime)
        val deserializedDateTime = json.decodeFromString<OffsetDateTime>(serializedDateTime)

        assertEquals(originalDateTime, deserializedDateTime)
    }

    @Test
    fun serialize_givenStandardTime_returnsExpected() {
        val dateTime = OffsetDateTime.of(2024, 4, 16, 15, 30, 15, 0, ZoneOffset.UTC)
        val expectedSerializedDateTime = "\"2024-04-16 15:30:15+00\""
        val serializedDateTime = json.encodeToString(dateTime)

        assertEquals(expectedSerializedDateTime, serializedDateTime)
    }

    @Test
    fun deserialize_givenAmTime_returnsExpected() {
        val dateTimeString = "\"2024-04-16 10:30:15+00\""
        val expectedDateTime = OffsetDateTime.of(2024, 4, 16, 10, 30, 15, 0, ZoneOffset.UTC)
        val deserializedDateTime = json.decodeFromString<OffsetDateTime>(dateTimeString)

        assertEquals(expectedDateTime, deserializedDateTime)
    }

    @Test
    fun deserialize_givenPmTime_returnsExpected() {
        val dateTimeString = "\"2024-04-16 16:30:15+00\""
        val expectedDateTime = OffsetDateTime.of(2024, 4, 16, 16, 30, 15, 0, ZoneOffset.UTC)
        val deserializedDateTime = json.decodeFromString<OffsetDateTime>(dateTimeString)

        assertEquals(expectedDateTime, deserializedDateTime)
    }

    @Test
    fun deserialize_givenZeroTime_returnsExpected() {
        val dateTimeString = "\"2024-04-16 00:00:00+00\""
        val expectedDateTime = OffsetDateTime.of(2024, 4, 16, 0, 0, 0, 0, ZoneOffset.UTC)
        val deserializedDateTime = json.decodeFromString<OffsetDateTime>(dateTimeString)

        assertEquals(expectedDateTime, deserializedDateTime)
    }


}