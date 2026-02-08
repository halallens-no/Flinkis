package com.halallens.flinkis.domain.model

/**
 * Aggregated weekly progress data for display in the weekly grid.
 */
data class WeeklyProgress(
    val weekStartDate: String,
    val weekEndDate: String,
    val dailyProgress: List<DayProgress>,
    val totalCompleted: Int,
    val totalRoutines: Int,
    val completionPercentage: Float
)

/**
 * Progress data for a single day.
 */
data class DayProgress(
    val date: String,
    val dayOfWeek: Int,
    val completedCount: Int,
    val totalCount: Int,
    val routineStatuses: List<RoutineStatus>
)

/**
 * Completion status for a single routine on a given day.
 */
data class RoutineStatus(
    val routineId: Long,
    val routineName: String,
    val completed: Boolean
)
