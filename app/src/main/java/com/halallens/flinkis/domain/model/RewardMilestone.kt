package com.halallens.flinkis.domain.model

/**
 * Domain model representing a reward milestone/badge.
 */
data class RewardMilestone(
    val id: Long = 0,
    val childId: Long,
    val name: String,
    val description: String,
    val requiredPoints: Int,
    val iconName: String,
    val isEarned: Boolean = false,
    val earnedAt: Long? = null
)
