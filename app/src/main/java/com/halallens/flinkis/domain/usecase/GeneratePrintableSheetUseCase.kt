package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.domain.model.Routine
import com.halallens.flinkis.util.DateUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class PrintableSheet(
    val childName: String,
    val weekStartDate: String,
    val weekEndDate: String,
    val routines: List<Routine>,
    val dayHeaders: List<String>
)

class GeneratePrintableSheetUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    suspend operator fun invoke(childId: Long, childName: String): PrintableSheet {
        val routines = routineRepository.getRoutinesForChild(childId).first()
        val weekDates = DateUtils.currentWeekDates()

        return PrintableSheet(
            childName = childName,
            weekStartDate = weekDates.first(),
            weekEndDate = weekDates.last(),
            routines = routines,
            dayHeaders = weekDates.map { DateUtils.formatDisplayDate(it) }
        )
    }
}
