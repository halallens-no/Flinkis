package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.ActivityLogRepository
import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.domain.model.Routine
import com.halallens.flinkis.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class RoutineWithCompletion(
    val routine: Routine,
    val isCompleted: Boolean
)

class GetTodayRoutinesUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val activityLogRepository: ActivityLogRepository
) {
    operator fun invoke(childId: Long): Flow<List<RoutineWithCompletion>> {
        val today = DateUtils.todayString()
        val dayOfWeek = DateUtils.todayDayOfWeek()

        return combine(
            routineRepository.getRoutinesForDay(childId, dayOfWeek),
            activityLogRepository.getLogsForDate(childId, today)
        ) { routines, logs ->
            val completedIds = logs.filter { it.completed }.map { it.routineId }.toSet()
            routines.map { routine ->
                RoutineWithCompletion(
                    routine = routine,
                    isCompleted = completedIds.contains(routine.id)
                )
            }
        }
    }
}
