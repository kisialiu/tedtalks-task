package com.io.uladzimir.tedtalks.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtil {
    private val months = mapOf(
        "January" to "01",
        "February" to "02",
        "March" to "03",
        "April" to "04",
        "May" to "05",
        "June" to "06",
        "July" to "07",
        "August" to "08",
        "September" to "09",
        "October" to "10",
        "November" to "11",
        "December" to "12"
    )
    private val currentYear = LocalDate.now().year

    fun monthYearToLocalDate(monthYear: String): LocalDate {
        val parts = monthYear.split(" ")
        val year = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Invalid year: ${parts[1]}")
        val month = months[parts[0]] ?: throw IllegalArgumentException("Invalid month: ${parts[0]}")

        if (year < 1900 || year > currentYear) {
            throw IllegalArgumentException("Year out of range: $year")
        }

        return LocalDate.parse("$year-$month-01", DateTimeFormatter.ISO_LOCAL_DATE)
    }
}