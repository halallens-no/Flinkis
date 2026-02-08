package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.ActivityLogRepository
import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.domain.model.DayProgress
import com.halallens.flinkis.domain.model.RoutineStatus
import com.halallens.flinkis.domain.model.WeeklyProgress
import com.halallens.flinkis.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetWeeklyProgressUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val activityLogRepository: ActivityLogRepository
) {
    operator fun invoke(childId: Long, weekOffset: Int = 0): Flow<WeeklyProgress> {
        val weekStart = DateUtils.weekStartDate().plusWeeks(weekOffset.toLong())
        val weekEnd = weekStart.plusDays(6)
        val startStr = weekStart.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val endStr = weekEnd.format(DateTimeFormatter.ISO_LOCAL_DATE)

        val weekDates = (0..6).map { weekStart.plusDays(it.toLong()).format(DateTimeFormatter.ISO_LOCAL_DATE) }

        return combine(
            routineRepository.getRoutinesForChild(childId),
            activityLogRepository.getLogsForDateRange(childId, startStr, endStr)
        ) { routines, logs ->
            val logsByDate = logs.groupBy { it.date }

            val dailyProgress = weekDates.mapIndexed { index, dateStr ->
                val date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
                val dayLogs = logsByDate[dateStr] ?: emptyList()
                val completedIds = dayLogs.filter { it.completed }.map { it.routineId }.toSet()

                val dayRoutines = routines.filter { it.daysOfWeek.contains(date.dayOfWeek.value) }

                DayProgress(
                    date = dateStr,
                    dayOfWeek = date.dayOfWeek.value,
                    completedCount = dayRoutines.count { completedIds.contains(it.id) },
                    totalCount = dayRoutines.size,
                    routineStatuses = dayRoutines.map { routine ->
                        RoutineStatus(
                            routineId = routine.id,
                            routineName = routine.name,
                            completed = completedIds.contains(routine.id)
                        )
                    }
                )
            }

            val totalCompleted = dailyProgress.sumOf { it.completedCount }
            val totalRoutines = dailyProgress.sumOf { it.totalCount }

            WeeklyProgress(
                weekStartDate = startStr,
                weekEndDate = endStr,
                dailyProgress = dailyProgress,
                totalCompleted = totalCompleted,
                totalRoutines = totalRoutines,
                completionPercentage = if (totalRoutines > 0) totalCompleted.toFloat() / totalRoutines else 0f
            )
        }
    }
}
