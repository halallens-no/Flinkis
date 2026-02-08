package com.halallens.flinkis.domain.model

/**
 * Domain model representing a completed activity entry.
 */
data class ActivityLog(
    val id: Long = 0,
    val routineId: Long,
    val childId: Long,
    val date: String,
    val completed: Boolean = false,
    val pointsEarned: Int = 0,
    val completedAt: Long? = null
)
