package com.halallens.flinkis.util

import com.halallens.flinkis.domain.model.TimeSlot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ConstantsTest {

    @Test
    fun `getDefaultRoutines returns 18 routines`() {
        val routines = Constants.getDefaultRoutines(1L)
        assertEquals(18, routines.size)
    }

    @Test
    fun `all default routines have correct childId`() {
        val childId = 42L
        val routines = Constants.getDefaultRoutines(childId)
        assertTrue(routines.all { it.childId == childId })
    }

    @Test
    fun `default routines cover all time slots`() {
        val routines = Constants.getDefaultRoutines(1L)
        val timeSlots = routines.map { it.timeSlot }.toSet()
        assertEquals(TimeSlot.entries.toSet(), timeSlots)
    }

    @Test
    fun `all default routines have positive points`() {
        val routines = Constants.getDefaultRoutines(1L)
        assertTrue(routines.all { it.points > 0 })
    }

    @Test
    fun `default routines have all 7 days active`() {
        val routines = Constants.getDefaultRoutines(1L)
        assertTrue(routines.all { it.daysOfWeek == listOf(1, 2, 3, 4, 5, 6, 7) })
    }

    @Test
    fun `morning slot has 5 routines`() {
        val routines = Constants.getDefaultRoutines(1L)
        assertEquals(5, routines.count { it.timeSlot == TimeSlot.MORNING })
    }

    @Test
    fun `bedtime slot has 4 routines`() {
        val routines = Constants.getDefaultRoutines(1L)
        assertEquals(4, routines.count { it.timeSlot == TimeSlot.BEDTIME })
    }
}
