package com.halallens.flinkis.data.repository

import com.halallens.flinkis.data.local.dao.ActivityLogDao
import com.halallens.flinkis.data.local.entity.ActivityLogEntity
import com.halallens.flinkis.domain.model.ActivityLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityLogRepository @Inject constructor(
    private val activityLogDao: ActivityLogDao
) {
    fun getLogsForDate(childId: Long, date: String): Flow<List<ActivityLog>> =
        activityLogDao.getLogsForDate(childId, date).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getLogsForDateRange(childId: Long, startDate: String, endDate: String): Flow<List<ActivityLog>> =
        activityLogDao.getLogsForDateRange(childId, startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getTotalPoints(childId: Long): Flow<Int> =
        activityLogDao.getTotalPoints(childId).map { it ?: 0 }

    suspend fun toggleCompletion(routineId: Long, childId: Long, date: String, points: Int): Boolean {
        val existing = activityLogDao.getLog(routineId, date)
        return if (existing != null) {
            val newCompleted = !existing.completed
            activityLogDao.update(
                existing.copy(
                    completed = newCompleted,
                    pointsEarned = if (newCompleted) points else 0,
                    completedAt = if (newCompleted) System.currentTimeMillis() else null
                )
            )
            newCompleted
        } else {
            activityLogDao.insert(
                ActivityLogEntity(
                    routineId = routineId,
                    childId = childId,
                    date = date,
                    completed = true,
                    pointsEarned = points,
                    completedAt = System.currentTimeMillis()
                )
            )
            true
        }
    }

    suspend fun getCompletedDates(childId: Long): List<String> =
        activityLogDao.getCompletedDates(childId)

    private fun ActivityLogEntity.toDomain() = ActivityLog(
        id = id,
        routineId = routineId,
        childId = childId,
        date = date,
        completed = completed,
        pointsEarned = pointsEarned,
        completedAt = completedAt
    )
}
