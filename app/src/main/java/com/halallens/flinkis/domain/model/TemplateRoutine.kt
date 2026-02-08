package com.halallens.flinkis.domain.model

data class TemplateRoutine(
    val name: String,
    val timeSlot: TimeSlot,
    val points: Int,
    val iconName: String,
    val daysOfWeek: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7)
)
