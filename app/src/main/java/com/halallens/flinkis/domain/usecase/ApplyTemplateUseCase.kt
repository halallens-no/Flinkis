package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.domain.model.Routine
import com.halallens.flinkis.domain.model.RoutineTemplate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplyTemplateUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    data class TemplateDiff(
        val removing: List<Routine>,
        val adding: List<Routine>
    )

    suspend fun previewDiff(template: RoutineTemplate, childId: Long): TemplateDiff {
        val current = routineRepository.getRoutineListForChild(childId)
        val incoming = toRoutines(template, childId)
        return TemplateDiff(removing = current, adding = incoming)
    }

    suspend fun apply(template: RoutineTemplate, childId: Long) {
        routineRepository.deleteAllRoutinesForChild(childId)
        val routines = toRoutines(template, childId)
        routineRepository.createRoutines(routines)
    }

    companion object {
        fun toRoutines(template: RoutineTemplate, childId: Long): List<Routine> {
            return template.routines.mapIndexed { index, tr ->
                Routine(
                    childId = childId,
                    name = tr.name,
                    timeSlot = tr.timeSlot,
                    points = tr.points,
                    iconName = tr.iconName,
                    isCustom = false,
                    sortOrder = index,
                    isActive = true,
                    daysOfWeek = tr.daysOfWeek
                )
            }
        }
    }
}
