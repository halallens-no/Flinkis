package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.util.Constants
import javax.inject.Inject

class SeedDefaultRoutinesUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    suspend operator fun invoke(childId: Long) {
        val count = routineRepository.getRoutineCount(childId)
        if (count == 0) {
            val defaults = Constants.getDefaultRoutines(childId)
            routineRepository.createRoutines(defaults)
        }
    }
}
