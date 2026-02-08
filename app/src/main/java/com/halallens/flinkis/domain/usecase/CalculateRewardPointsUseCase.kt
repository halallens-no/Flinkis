package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.ActivityLogRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class RewardSummary(
    val totalPoints: Int,
    val currentStreak: Int
)

class CalculateRewardPointsUseCase @Inject constructor(
    private val activityLogRepository: ActivityLogRepository
) {
    fun getTotalPoints(childId: Long): Flow<Int> =
        activityLogRepository.getTotalPoints(childId)

    suspend fun calculateStreak(childId: Long): Int {
        val completedDates = activityLogRepository.getCompletedDates(childId)
        if (completedDates.isEmpty()) return 0

        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val sortedDates = completedDates
            .map { LocalDate.parse(it, formatter) }
            .sortedDescending()

        var streak = 0
        var expectedDate = LocalDate.now()

        for (date in sortedDates) {
            if (date == expectedDate) {
                streak++
                expectedDate = expectedDate.minusDays(1)
            } else if (date.isBefore(expectedDate)) {
                break
            }
        }
        return streak
    }
}
