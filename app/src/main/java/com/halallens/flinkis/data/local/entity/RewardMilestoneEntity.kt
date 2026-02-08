package com.halallens.flinkis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_milestones")
data class RewardMilestoneEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val name: String,
    val description: String,
    val requiredPoints: Int,
    val iconName: String,
    val isEarned: Boolean = false,
    val earnedAt: Long? = null
)
