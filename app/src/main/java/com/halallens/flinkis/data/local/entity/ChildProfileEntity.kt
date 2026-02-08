package com.halallens.flinkis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val avatarId: Int,
    val themeType: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
