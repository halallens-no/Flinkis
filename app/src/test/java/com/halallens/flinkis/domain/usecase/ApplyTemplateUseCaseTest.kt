package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.domain.model.RoutineTemplate
import com.halallens.flinkis.domain.model.TemplateCategory
import com.halallens.flinkis.domain.model.TemplateRoutine
import com.halallens.flinkis.domain.model.TimeSlot
import org.junit.Assert.*
import org.junit.Test

class ApplyTemplateUseCaseTest {

    @Test
    fun `toRoutines maps template routines to child routines`() {
        val template = RoutineTemplate(
            id = -1,
            name = "Test",
            description = "Test template",
            category = TemplateCategory.GENERAL,
            iconName = "star",
            routines = listOf(
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Homework", TimeSlot.SCHOOL, 3, "book")
            )
        )

        val routines = ApplyTemplateUseCase.toRoutines(template = template, childId = 42L)

        assertEquals(2, routines.size)
        assertEquals(42L, routines[0].childId)
        assertEquals("Wake up", routines[0].name)
        assertEquals(TimeSlot.MORNING, routines[0].timeSlot)
        assertEquals(false, routines[0].isCustom)
        assertEquals(0, routines[0].sortOrder)
        assertEquals(1, routines[1].sortOrder)
    }
}
