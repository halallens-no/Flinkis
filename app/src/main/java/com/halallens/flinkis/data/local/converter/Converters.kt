package com.halallens.flinkis.data.local.converter

import androidx.room.TypeConverter

/**
 * Room TypeConverters for custom types.
 */
class Converters {

    @TypeConverter
    fun fromDaysOfWeek(days: String): List<Int> {
        return days.split(",").mapNotNull { it.trim().toIntOrNull() }
    }

    @TypeConverter
    fun toDaysOfWeek(days: List<Int>): String {
        return days.joinToString(",")
    }
}
