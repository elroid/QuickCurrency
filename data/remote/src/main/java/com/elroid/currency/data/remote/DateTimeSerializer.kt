package com.elroid.currency.data.remote

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Serializer for dates with date, time, and offset, example: 2023-03-21 12:43:00+00
 * Also deals with non-standard "2023-03-21 00:00:00+00+00" format that has cropped up in the API
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = OffsetDateTime::class)
object DateTimeSerializer : KSerializer<OffsetDateTime> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx")
    private val formatterMulti = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx[+00]")

    override fun serialize(encoder: Encoder, value: OffsetDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): OffsetDateTime {
        val decodedString = decoder.decodeString()
        return try {
            OffsetDateTime.parse(decodedString, formatter)
        } catch (e: DateTimeParseException) {
            OffsetDateTime.parse(decodedString, formatterMulti)
        }
    }
}