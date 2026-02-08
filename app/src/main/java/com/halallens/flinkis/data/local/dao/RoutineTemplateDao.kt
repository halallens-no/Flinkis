package com.halallens.flinkis.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.halallens.flinkis.data.local.entity.RoutineTemplateEntity
import com.halallens.flinkis.data.local.entity.TemplateRoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineTemplateDao {

    @Query("SELECT * FROM routine_templates ORDER BY createdAt DESC")
    fun getAllTemplates(): Flow<List<RoutineTemplateEntity>>

    @Query("SELECT * FROM routine_templates WHERE id = :templateId")
    suspend fun getTemplateById(templateId: Long): RoutineTemplateEntity?

    @Query("SELECT * FROM template_routines WHERE templateId = :templateId ORDER BY sortOrder")
    suspend fun getRoutinesForTemplate(templateId: Long): List<TemplateRoutineEntity>

    @Insert
    suspend fun insertTemplate(template: RoutineTemplateEntity): Long

    @Insert
    suspend fun insertTemplateRoutines(routines: List<TemplateRoutineEntity>)

    @Update
    suspend fun updateTemplate(template: RoutineTemplateEntity)

    @Delete
    suspend fun deleteTemplate(template: RoutineTemplateEntity)

    @Query("DELETE FROM template_routines WHERE templateId = :templateId")
    suspend fun deleteTemplateRoutines(templateId: Long)

    @Query("SELECT COUNT(*) FROM routine_templates")
    suspend fun getTemplateCount(): Int
}
