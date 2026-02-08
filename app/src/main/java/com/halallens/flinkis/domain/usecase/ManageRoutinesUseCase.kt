package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.domain.model.Routine
import javax.inject.Inject

class ManageRoutinesUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    suspend fun addRoutine(routine: Routine): Long =
        routineRepository.createRoutine(routine.copy(isCustom = true))

    suspend fun updateRoutine(routine: Routine) =
        routineRepository.updateRoutine(routine)

    suspend fun deleteRoutine(routine: Routine) =
        routineRepository.deleteRoutine(routine)
}
