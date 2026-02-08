package com.halallens.flinkis.domain.model

/**
 * Time slots for grouping daily routines.
 */
enum class TimeSlot(val displayName: String, val iconName: String) {
    MORNING("Morning", "wb_sunny"),
    SCHOOL("School", "school"),
    AFTERNOON("Afternoon", "wb_cloudy"),
    EVENING("Evening", "nights_stay"),
    BEDTIME("Bedtime", "bedtime")
}
