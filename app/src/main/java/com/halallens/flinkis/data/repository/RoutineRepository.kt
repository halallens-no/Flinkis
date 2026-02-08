package com.halallens.flinkis.data.repository

import com.halallens.flinkis.data.local.dao.RoutineDao
import com.halallens.flinkis.data.local.entity.RoutineEntity
import com.halallens.flinkis.domain.model.Routine
import com.halallens.flinkis.domain.model.TimeSlot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoutineRepository @Inject constructor(
    private val routineDao: RoutineDao
) {
    fun getRoutinesForChild(childId: Long): Flow<List<Routine>> =
        routineDao.getRoutinesForChild(childId).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getRoutinesForDay(childId: Long, dayOfWeek: Int): Flow<List<Routine>> =
        routineDao.getRoutinesForDay(childId, dayOfWeek).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun createRoutine(routine: Routine): Long =
        routineDao.insert(routine.toEntity())

    suspend fun createRoutines(routines: List<Routine>) =
        routineDao.insertAll(routines.map { it.toEntity() })

    suspend fun updateRoutine(routine: Routine) =
        routineDao.update(routine.toEntity())

    suspend fun deleteRoutine(routine: Routine) =
        routineDao.delete(routine.toEntity())

    suspend fun getRoutineCount(childId: Long): Int =
        routineDao.getRoutineCount(childId)

    suspend fun getRoutineListForChild(childId: Long): List<Routine> {
        return routineDao.getRoutineListForChild(childId).map { it.toDomain() }
    }

    suspend fun deleteAllRoutinesForChild(childId: Long) {
        routineDao.deleteAllRoutinesForChild(childId)
    }

    private fun RoutineEntity.toDomain() = Routine(
        id = id,
        childId = childId,
        name = name,
        timeSlot = TimeSlot.valueOf(timeSlot),
        points = points,
        iconName = iconName,
        isCustom = isCustom,
        sortOrder = sortOrder,
        isActive = isActive,
        daysOfWeek = daysOfWeek.split(",").mapNotNull { it.trim().toIntOrNull() }
    )

    private fun Routine.toEntity() = RoutineEntity(
        id = id,
        childId = childId,
        name = name,
        timeSlot = timeSlot.name,
        points = points,
        iconName = iconName,
        isCustom = isCustom,
        sortOrder = sortOrder,
        isActive = isActive,
        daysOfWeek = daysOfWeek.joinToString(",")
    )
}
