package com.halallens.flinkis.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "template_routines",
    foreignKeys = [ForeignKey(
        entity = RoutineTemplateEntity::class,
        parentColumns = ["id"],
        childColumns = ["templateId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("templateId")]
)
data class TemplateRoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long,
    val name: String,
    val timeSlot: String,
    val points: Int = 1,
    val iconName: String,
    val daysOfWeek: String = "1,2,3,4,5,6,7",
    val sortOrder: Int = 0
)
