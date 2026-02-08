package com.halallens.flinkis.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activity_logs",
    indices = [Index(value = ["routineId", "date"], unique = true)]
)
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val routineId: Long,
    val childId: Long,
    val date: String,
    val completed: Boolean = false,
    val pointsEarned: Int = 0,
    val completedAt: Long? = null
)
