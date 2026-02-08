package com.halallens.flinkis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val name: String,
    val timeSlot: String,
    val points: Int = 1,
    val iconName: String = "check_circle",
    val isCustom: Boolean = false,
    val sortOrder: Int = 0,
    val isActive: Boolean = true,
    val daysOfWeek: String = "1,2,3,4,5,6,7"
)
