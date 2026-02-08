package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.ActivityLogRepository
import com.halallens.flinkis.util.DateUtils
import javax.inject.Inject

class ToggleActivityCompletionUseCase @Inject constructor(
    private val activityLogRepository: ActivityLogRepository
) {
    suspend operator fun invoke(routineId: Long, childId: Long, points: Int): Boolean {
        val today = DateUtils.todayString()
        return activityLogRepository.toggleCompletion(routineId, childId, today, points)
    }
}
