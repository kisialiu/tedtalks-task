package com.io.uladzimir.tedtalks.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class MonthYearDeserializer : JsonDeserializer<LocalDate>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate {
        val dateString = p.text
        return try {
            LocalDate.parse("$dateString 01", DateTimeFormatter.ofPattern("MMMM yyyy dd", Locale.ENGLISH))
        } catch (e: DateTimeParseException) {
            throw IOException("Invalid date format: $dateString", e)
        }
    }
}