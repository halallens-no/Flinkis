package com.halallens.flinkis.domain.model

/**
 * Domain model representing a daily routine/activity.
 */
data class Routine(
    val id: Long = 0,
    val childId: Long,
    val name: String,
    val timeSlot: TimeSlot,
    val points: Int = 1,
    val iconName: String = "check_circle",
    val isCustom: Boolean = false,
    val sortOrder: Int = 0,
    val isActive: Boolean = true,
    val daysOfWeek: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7)
)
