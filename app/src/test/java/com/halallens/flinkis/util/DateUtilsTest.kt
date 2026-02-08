package com.halallens.flinkis.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateUtilsTest {

    @Test
    fun `todayString returns ISO formatted date`() {
        val result = DateUtils.todayString()
        assertNotNull(result)
        val parsed = LocalDate.parse(result, DateTimeFormatter.ISO_LOCAL_DATE)
        assertEquals(LocalDate.now(), parsed)
    }

    @Test
    fun `todayDayOfWeek returns value between 1 and 7`() {
        val result = DateUtils.todayDayOfWeek()
        assertTrue("Day of week should be 1-7, got $result", result in 1..7)
    }

    @Test
    fun `weekStartDate returns Monday`() {
        val weekStart = DateUtils.weekStartDate()
        assertEquals(DayOfWeek.MONDAY, weekStart.dayOfWeek)
    }

    @Test
    fun `weekEndDate returns Sunday`() {
        val weekEnd = DateUtils.weekEndDate()
        assertEquals(DayOfWeek.SUNDAY, weekEnd.dayOfWeek)
    }

    @Test
    fun `weekEndDate is 6 days after weekStartDate`() {
        val start = DateUtils.weekStartDate()
        val end = DateUtils.weekEndDate()
        assertEquals(6, java.time.temporal.ChronoUnit.DAYS.between(start, end))
    }

    @Test
    fun `currentWeekDates returns 7 dates`() {
        val dates = DateUtils.currentWeekDates()
        assertEquals(7, dates.size)
    }

    @Test
    fun `currentWeekDates are all valid ISO dates`() {
        val dates = DateUtils.currentWeekDates()
        dates.forEach { dateStr ->
            val parsed = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
            assertNotNull(parsed)
        }
    }

    @Test
    fun `currentWeekDates starts on Monday`() {
        val dates = DateUtils.currentWeekDates()
        val firstDate = LocalDate.parse(dates.first(), DateTimeFormatter.ISO_LOCAL_DATE)
        assertEquals(DayOfWeek.MONDAY, firstDate.dayOfWeek)
    }

    @Test
    fun `currentWeekDates ends on Sunday`() {
        val dates = DateUtils.currentWeekDates()
        val lastDate = LocalDate.parse(dates.last(), DateTimeFormatter.ISO_LOCAL_DATE)
        assertEquals(DayOfWeek.SUNDAY, lastDate.dayOfWeek)
    }

    @Test
    fun `formatDisplayDate returns readable format`() {
        val result = DateUtils.formatDisplayDate("2026-02-07")
        assertNotNull(result)
        assertTrue("Should contain month info, got: $result", result.isNotEmpty())
    }
}
