package com.halallens.flinkis.domain.model

/**
 * Domain model representing a child's profile.
 */
data class ChildProfile(
    val id: Long = 0,
    val name: String,
    val avatarId: Int,
    val themeType: ThemeType,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
