package com.halallens.flinkis.data.repository

import com.halallens.flinkis.data.local.dao.RoutineTemplateDao
import com.halallens.flinkis.data.local.entity.RoutineTemplateEntity
import com.halallens.flinkis.data.local.entity.TemplateRoutineEntity
import com.halallens.flinkis.domain.model.RoutineTemplate
import com.halallens.flinkis.domain.model.TemplateCategory
import com.halallens.flinkis.domain.model.TemplateRoutine
import com.halallens.flinkis.domain.model.TimeSlot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplateRepository @Inject constructor(
    private val templateDao: RoutineTemplateDao
) {
    fun getCustomTemplates(): Flow<List<RoutineTemplate>> {
        return templateDao.getAllTemplates().map { entities ->
            entities.map { entity ->
                val routines = templateDao.getRoutinesForTemplate(entity.id)
                entity.toDomain(routines)
            }
        }
    }

    suspend fun getTemplateById(id: Long): RoutineTemplate? {
        val entity = templateDao.getTemplateById(id) ?: return null
        val routines = templateDao.getRoutinesForTemplate(id)
        return entity.toDomain(routines)
    }

    suspend fun saveTemplate(template: RoutineTemplate): Long {
        val templateId = templateDao.insertTemplate(
            RoutineTemplateEntity(
                name = template.name,
                description = template.description,
                category = template.category.name,
                iconName = template.iconName
            )
        )
        val routineEntities = template.routines.mapIndexed { index, routine ->
            TemplateRoutineEntity(
                templateId = templateId,
                name = routine.name,
                timeSlot = routine.timeSlot.name,
                points = routine.points,
                iconName = routine.iconName,
                daysOfWeek = routine.daysOfWeek.joinToString(","),
                sortOrder = index
            )
        }
        templateDao.insertTemplateRoutines(routineEntities)
        return templateId
    }

    suspend fun deleteTemplate(templateId: Long) {
        val entity = templateDao.getTemplateById(templateId) ?: return
        templateDao.deleteTemplate(entity)
    }

    private fun RoutineTemplateEntity.toDomain(
        routines: List<TemplateRoutineEntity>
    ): RoutineTemplate {
        return RoutineTemplate(
            id = id,
            name = name,
            description = description,
            category = TemplateCategory.valueOf(category),
            iconName = iconName,
            isBuiltIn = false,
            routines = routines.map { r ->
                TemplateRoutine(
                    name = r.name,
                    timeSlot = TimeSlot.valueOf(r.timeSlot),
                    points = r.points,
                    iconName = r.iconName,
                    daysOfWeek = r.daysOfWeek.split(",").map { it.trim().toInt() }
                )
            },
            createdAt = createdAt
        )
    }
}
