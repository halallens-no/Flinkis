package com.halallens.flinkis.domain.model

data class RoutineTemplate(
    val id: Long = 0,
    val name: String,
    val description: String,
    val category: TemplateCategory,
    val iconName: String,
    val routines: List<TemplateRoutine>,
    val isBuiltIn: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
