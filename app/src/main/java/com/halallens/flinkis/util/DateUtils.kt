package com.halallens.flinkis.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Date formatting utilities for MyRoutine.
 */
object DateUtils {

    private val ISO_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE

    /**
     * Today's date as ISO string (e.g., "2026-02-07")
     */
    fun todayString(): String = LocalDate.now().format(ISO_DATE_FORMAT)

    /**
     * Today's day of week as integer (1=Monday, 7=Sunday)
     */
    fun todayDayOfWeek(): Int = LocalDate.now().dayOfWeek.value

    /**
     * Get the start of the current week (Monday)
     */
    fun weekStartDate(): LocalDate {
        val today = LocalDate.now()
        return today.minusDays((today.dayOfWeek.value - DayOfWeek.MONDAY.value).toLong())
    }

    /**
     * Get the end of the current week (Sunday)
     */
    fun weekEndDate(): LocalDate = weekStartDate().plusDays(6)

    /**
     * Format a date for display (e.g., "Mon, Feb 7")
     */
    fun formatDisplayDate(dateString: String): String {
        val date = LocalDate.parse(dateString, ISO_DATE_FORMAT)
        return date.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
    }

    /**
     * Get dates for the current week as ISO strings.
     */
    fun currentWeekDates(): List<String> {
        val start = weekStartDate()
        return (0..6).map { start.plusDays(it.toLong()).format(ISO_DATE_FORMAT) }
    }
}
