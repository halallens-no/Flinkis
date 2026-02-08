package com.halallens.flinkis.util

import com.halallens.flinkis.domain.model.Routine
import com.halallens.flinkis.domain.model.RoutineTemplate
import com.halallens.flinkis.domain.model.TemplateCategory
import com.halallens.flinkis.domain.model.TemplateRoutine
import com.halallens.flinkis.domain.model.TimeSlot

/**
 * App constants and default data.
 */
object Constants {

    const val DATABASE_NAME = "myroutine_database"
    const val PREFERENCES_NAME = "myroutine_preferences"

    /**
     * Generate default routines for a new child.
     */
    fun getDefaultRoutines(childId: Long): List<Routine> = listOf(
        // Morning routines
        Routine(childId = childId, name = "Wake up", timeSlot = TimeSlot.MORNING, points = 1, iconName = "alarm", sortOrder = 0),
        Routine(childId = childId, name = "Make bed", timeSlot = TimeSlot.MORNING, points = 1, iconName = "bed", sortOrder = 1),
        Routine(childId = childId, name = "Brush teeth", timeSlot = TimeSlot.MORNING, points = 1, iconName = "brush", sortOrder = 2),
        Routine(childId = childId, name = "Get dressed", timeSlot = TimeSlot.MORNING, points = 1, iconName = "checkroom", sortOrder = 3),
        Routine(childId = childId, name = "Eat breakfast", timeSlot = TimeSlot.MORNING, points = 2, iconName = "restaurant", sortOrder = 4),

        // School routines
        Routine(childId = childId, name = "Pack school bag", timeSlot = TimeSlot.SCHOOL, points = 1, iconName = "backpack", sortOrder = 0),
        Routine(childId = childId, name = "Do homework", timeSlot = TimeSlot.SCHOOL, points = 3, iconName = "menu_book", sortOrder = 1),
        Routine(childId = childId, name = "Read for 15 minutes", timeSlot = TimeSlot.SCHOOL, points = 2, iconName = "auto_stories", sortOrder = 2),

        // Afternoon routines
        Routine(childId = childId, name = "Outdoor play", timeSlot = TimeSlot.AFTERNOON, points = 2, iconName = "park", sortOrder = 0),
        Routine(childId = childId, name = "Clean up toys", timeSlot = TimeSlot.AFTERNOON, points = 1, iconName = "toys", sortOrder = 1),
        Routine(childId = childId, name = "Help with chores", timeSlot = TimeSlot.AFTERNOON, points = 2, iconName = "cleaning_services", sortOrder = 2),

        // Evening routines
        Routine(childId = childId, name = "Eat dinner", timeSlot = TimeSlot.EVENING, points = 1, iconName = "dinner_dining", sortOrder = 0),
        Routine(childId = childId, name = "Help set table", timeSlot = TimeSlot.EVENING, points = 1, iconName = "table_restaurant", sortOrder = 1),
        Routine(childId = childId, name = "Screen-free time", timeSlot = TimeSlot.EVENING, points = 2, iconName = "phonelink_off", sortOrder = 2),

        // Bedtime routines
        Routine(childId = childId, name = "Brush teeth", timeSlot = TimeSlot.BEDTIME, points = 1, iconName = "brush", sortOrder = 0),
        Routine(childId = childId, name = "Put on pajamas", timeSlot = TimeSlot.BEDTIME, points = 1, iconName = "checkroom", sortOrder = 1),
        Routine(childId = childId, name = "Read bedtime story", timeSlot = TimeSlot.BEDTIME, points = 2, iconName = "auto_stories", sortOrder = 2),
        Routine(childId = childId, name = "Lights out on time", timeSlot = TimeSlot.BEDTIME, points = 1, iconName = "bedtime", sortOrder = 3)
    )

    /**
     * 6 built-in routine templates: 3 Islamic + 3 General.
     * Negative IDs distinguish built-in templates from user-created ones.
     */
    val BUILT_IN_TEMPLATES: List<RoutineTemplate> = listOf(

        // ── Islamic Templates ──────────────────────────────────────────

        RoutineTemplate(
            id = -1,
            name = "Ramadan Routine",
            description = "Special schedule for the holy month with suhoor, prayers, and iftar",
            category = TemplateCategory.ISLAMIC,
            iconName = "mosque",
            routines = listOf(
                // Morning
                TemplateRoutine("Wake for Suhoor", TimeSlot.MORNING, 2, "alarm"),
                TemplateRoutine("Brush teeth", TimeSlot.MORNING, 1, "brush"),
                TemplateRoutine("Fajr prayer", TimeSlot.MORNING, 3, "mosque"),
                // School
                TemplateRoutine("Pack school bag", TimeSlot.SCHOOL, 1, "backpack"),
                TemplateRoutine("Homework", TimeSlot.SCHOOL, 3, "menu_book"),
                TemplateRoutine("Quran reading 15 min", TimeSlot.SCHOOL, 3, "auto_stories"),
                // Afternoon
                TemplateRoutine("Dhuhr prayer", TimeSlot.AFTERNOON, 2, "mosque"),
                TemplateRoutine("Asr prayer", TimeSlot.AFTERNOON, 2, "mosque"),
                TemplateRoutine("Help prepare Iftar", TimeSlot.AFTERNOON, 2, "restaurant"),
                // Evening
                TemplateRoutine("Iftar on time", TimeSlot.EVENING, 2, "dinner_dining"),
                TemplateRoutine("Maghrib prayer", TimeSlot.EVENING, 2, "mosque"),
                TemplateRoutine("Isha prayer", TimeSlot.EVENING, 2, "mosque"),
                // Bedtime
                TemplateRoutine("Taraweeh / Witr", TimeSlot.BEDTIME, 3, "mosque"),
                TemplateRoutine("Dua before sleep", TimeSlot.BEDTIME, 1, "bedtime"),
                TemplateRoutine("Lights out on time", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        RoutineTemplate(
            id = -2,
            name = "Daily Islamic",
            description = "Everyday routine with five daily prayers and Quran time",
            category = TemplateCategory.ISLAMIC,
            iconName = "auto_stories",
            routines = listOf(
                // Morning
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Fajr prayer", TimeSlot.MORNING, 3, "mosque"),
                TemplateRoutine("Brush teeth", TimeSlot.MORNING, 1, "brush"),
                TemplateRoutine("Get dressed", TimeSlot.MORNING, 1, "checkroom"),
                TemplateRoutine("Eat breakfast", TimeSlot.MORNING, 2, "restaurant"),
                // School
                TemplateRoutine("Dhuhr prayer", TimeSlot.SCHOOL, 2, "mosque"),
                TemplateRoutine("Do homework", TimeSlot.SCHOOL, 3, "menu_book"),
                TemplateRoutine("Quran reading 15 min", TimeSlot.SCHOOL, 3, "auto_stories"),
                // Afternoon
                TemplateRoutine("Asr prayer", TimeSlot.AFTERNOON, 2, "mosque"),
                TemplateRoutine("Outdoor play", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Help with chores", TimeSlot.AFTERNOON, 2, "cleaning_services"),
                // Evening
                TemplateRoutine("Maghrib prayer", TimeSlot.EVENING, 2, "mosque"),
                TemplateRoutine("Eat dinner", TimeSlot.EVENING, 1, "dinner_dining"),
                TemplateRoutine("Isha prayer", TimeSlot.EVENING, 2, "mosque"),
                TemplateRoutine("Screen-free time", TimeSlot.EVENING, 2, "phonelink_off"),
                // Bedtime
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Dua before sleep", TimeSlot.BEDTIME, 1, "bedtime"),
                TemplateRoutine("Read bedtime story", TimeSlot.BEDTIME, 2, "auto_stories"),
                TemplateRoutine("Lights out on time", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        RoutineTemplate(
            id = -3,
            name = "Weekend Islamic",
            description = "Relaxed weekend with prayers, Quran, and family time",
            category = TemplateCategory.ISLAMIC,
            iconName = "wb_sunny",
            routines = listOf(
                // Morning
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Fajr prayer", TimeSlot.MORNING, 3, "mosque"),
                TemplateRoutine("Breakfast", TimeSlot.MORNING, 1, "restaurant"),
                TemplateRoutine("Quran 20 min", TimeSlot.MORNING, 3, "auto_stories"),
                // Afternoon
                TemplateRoutine("Dhuhr prayer", TimeSlot.AFTERNOON, 2, "mosque"),
                TemplateRoutine("Outdoor play", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Asr prayer", TimeSlot.AFTERNOON, 2, "mosque"),
                TemplateRoutine("Help with chores", TimeSlot.AFTERNOON, 2, "cleaning_services"),
                // Evening
                TemplateRoutine("Maghrib prayer", TimeSlot.EVENING, 2, "mosque"),
                TemplateRoutine("Family time", TimeSlot.EVENING, 2, "people"),
                TemplateRoutine("Isha prayer", TimeSlot.EVENING, 2, "mosque"),
                // Bedtime
                TemplateRoutine("Dua before sleep", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        // ── General Templates ──────────────────────────────────────────

        RoutineTemplate(
            id = -4,
            name = "School Day",
            description = "Standard weekday routine for school-age kids",
            category = TemplateCategory.GENERAL,
            iconName = "school",
            routines = listOf(
                // Morning
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Make bed", TimeSlot.MORNING, 1, "bed"),
                TemplateRoutine("Brush teeth", TimeSlot.MORNING, 1, "brush"),
                TemplateRoutine("Get dressed", TimeSlot.MORNING, 1, "checkroom"),
                TemplateRoutine("Eat breakfast", TimeSlot.MORNING, 2, "restaurant"),
                // School
                TemplateRoutine("Pack school bag", TimeSlot.SCHOOL, 1, "backpack"),
                TemplateRoutine("Do homework", TimeSlot.SCHOOL, 3, "menu_book"),
                TemplateRoutine("Read for 15 minutes", TimeSlot.SCHOOL, 2, "auto_stories"),
                // Afternoon
                TemplateRoutine("Outdoor play", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Clean up toys", TimeSlot.AFTERNOON, 1, "toys"),
                TemplateRoutine("Help with chores", TimeSlot.AFTERNOON, 2, "cleaning_services"),
                // Evening
                TemplateRoutine("Eat dinner", TimeSlot.EVENING, 1, "dinner_dining"),
                TemplateRoutine("Help set table", TimeSlot.EVENING, 1, "table_restaurant"),
                TemplateRoutine("Screen-free time", TimeSlot.EVENING, 2, "phonelink_off"),
                // Bedtime
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Put on pajamas", TimeSlot.BEDTIME, 1, "checkroom"),
                TemplateRoutine("Read bedtime story", TimeSlot.BEDTIME, 2, "auto_stories"),
                TemplateRoutine("Lights out on time", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        RoutineTemplate(
            id = -5,
            name = "Weekend Fun",
            description = "Relaxed weekend with play, creativity, and family",
            category = TemplateCategory.GENERAL,
            iconName = "emoji_nature",
            routines = listOf(
                // Morning
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Make bed", TimeSlot.MORNING, 1, "bed"),
                TemplateRoutine("Breakfast", TimeSlot.MORNING, 1, "restaurant"),
                // Afternoon
                TemplateRoutine("Outdoor play", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Creative time", TimeSlot.AFTERNOON, 2, "palette"),
                TemplateRoutine("Clean up toys", TimeSlot.AFTERNOON, 1, "toys"),
                TemplateRoutine("Help with chores", TimeSlot.AFTERNOON, 2, "cleaning_services"),
                // Evening
                TemplateRoutine("Dinner", TimeSlot.EVENING, 1, "dinner_dining"),
                TemplateRoutine("Screen-free time", TimeSlot.EVENING, 2, "phonelink_off"),
                // Bedtime
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush")
            )
        ),

        RoutineTemplate(
            id = -6,
            name = "Summer Break",
            description = "Balanced summer days with learning, play, and family",
            category = TemplateCategory.GENERAL,
            iconName = "wb_sunny",
            routines = listOf(
                // Morning
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Make bed", TimeSlot.MORNING, 1, "bed"),
                TemplateRoutine("Brush teeth", TimeSlot.MORNING, 1, "brush"),
                TemplateRoutine("Breakfast", TimeSlot.MORNING, 1, "restaurant"),
                // Afternoon
                TemplateRoutine("Read 20 min", TimeSlot.AFTERNOON, 3, "auto_stories"),
                TemplateRoutine("Outdoor play", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Learn something new", TimeSlot.AFTERNOON, 3, "lightbulb"),
                TemplateRoutine("Help with chores", TimeSlot.AFTERNOON, 2, "cleaning_services"),
                // Evening
                TemplateRoutine("Dinner", TimeSlot.EVENING, 1, "dinner_dining"),
                TemplateRoutine("Family activity", TimeSlot.EVENING, 2, "people"),
                // Bedtime
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Lights out on time", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        RoutineTemplate(
            id = -7,
            name = "Active Sports Kid",
            description = "For sporty kids with training, warm-ups, and recovery",
            category = TemplateCategory.GENERAL,
            iconName = "sports_soccer",
            routines = listOf(
                TemplateRoutine("Wake up early", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Healthy breakfast", TimeSlot.MORNING, 2, "restaurant"),
                TemplateRoutine("Stretch & warm-up", TimeSlot.MORNING, 2, "fitness_center"),
                TemplateRoutine("Homework", TimeSlot.SCHOOL, 3, "menu_book"),
                TemplateRoutine("Pack sports bag", TimeSlot.SCHOOL, 1, "backpack"),
                TemplateRoutine("Sports practice", TimeSlot.AFTERNOON, 3, "sports_soccer"),
                TemplateRoutine("Cool-down stretches", TimeSlot.AFTERNOON, 1, "self_improvement"),
                TemplateRoutine("Shower", TimeSlot.AFTERNOON, 1, "shower"),
                TemplateRoutine("Healthy dinner", TimeSlot.EVENING, 2, "dinner_dining"),
                TemplateRoutine("Prepare gear for tomorrow", TimeSlot.EVENING, 1, "backpack"),
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Lights out on time", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        RoutineTemplate(
            id = -8,
            name = "Little Explorer",
            description = "Simple routine for toddlers and preschoolers (ages 3-5)",
            category = TemplateCategory.GENERAL,
            iconName = "child_care",
            routines = listOf(
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Brush teeth", TimeSlot.MORNING, 1, "brush"),
                TemplateRoutine("Get dressed", TimeSlot.MORNING, 1, "checkroom"),
                TemplateRoutine("Eat breakfast", TimeSlot.MORNING, 1, "restaurant"),
                TemplateRoutine("Play time", TimeSlot.AFTERNOON, 2, "toys"),
                TemplateRoutine("Snack time", TimeSlot.AFTERNOON, 1, "restaurant"),
                TemplateRoutine("Outdoor time", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Dinner", TimeSlot.EVENING, 1, "dinner_dining"),
                TemplateRoutine("Bath time", TimeSlot.BEDTIME, 1, "shower"),
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Bedtime story", TimeSlot.BEDTIME, 2, "auto_stories")
            )
        ),

        RoutineTemplate(
            id = -9,
            name = "Homework Hero",
            description = "Focused on study habits, reading, and academic success",
            category = TemplateCategory.GENERAL,
            iconName = "school",
            routines = listOf(
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Breakfast", TimeSlot.MORNING, 1, "restaurant"),
                TemplateRoutine("Review today's plan", TimeSlot.MORNING, 2, "checklist"),
                TemplateRoutine("Pack school bag", TimeSlot.SCHOOL, 1, "backpack"),
                TemplateRoutine("Do homework", TimeSlot.SCHOOL, 3, "menu_book"),
                TemplateRoutine("Read for 20 min", TimeSlot.SCHOOL, 3, "auto_stories"),
                TemplateRoutine("Practice handwriting", TimeSlot.SCHOOL, 2, "edit"),
                TemplateRoutine("Outdoor break", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Review what I learned", TimeSlot.AFTERNOON, 2, "lightbulb"),
                TemplateRoutine("Dinner", TimeSlot.EVENING, 1, "dinner_dining"),
                TemplateRoutine("Prepare bag for tomorrow", TimeSlot.EVENING, 1, "backpack"),
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Read before bed", TimeSlot.BEDTIME, 2, "auto_stories"),
                TemplateRoutine("Lights out on time", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        RoutineTemplate(
            id = -10,
            name = "Chore Champion",
            description = "Teaches responsibility through age-appropriate chores",
            category = TemplateCategory.GENERAL,
            iconName = "cleaning_services",
            routines = listOf(
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Make bed", TimeSlot.MORNING, 1, "bed"),
                TemplateRoutine("Tidy room", TimeSlot.MORNING, 2, "cleaning_services"),
                TemplateRoutine("Breakfast", TimeSlot.MORNING, 1, "restaurant"),
                TemplateRoutine("Homework", TimeSlot.SCHOOL, 3, "menu_book"),
                TemplateRoutine("Set the table", TimeSlot.AFTERNOON, 1, "table_restaurant"),
                TemplateRoutine("Water plants", TimeSlot.AFTERNOON, 1, "park"),
                TemplateRoutine("Clean up toys", TimeSlot.AFTERNOON, 1, "toys"),
                TemplateRoutine("Help cook dinner", TimeSlot.EVENING, 2, "restaurant"),
                TemplateRoutine("Clear the table", TimeSlot.EVENING, 1, "table_restaurant"),
                TemplateRoutine("Sort laundry", TimeSlot.EVENING, 1, "cleaning_services"),
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Lights out on time", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        RoutineTemplate(
            id = -11,
            name = "Healthy Habits",
            description = "Focus on nutrition, exercise, hygiene, and mindfulness",
            category = TemplateCategory.GENERAL,
            iconName = "favorite",
            routines = listOf(
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Drink water", TimeSlot.MORNING, 1, "water_drop"),
                TemplateRoutine("Brush teeth", TimeSlot.MORNING, 1, "brush"),
                TemplateRoutine("Healthy breakfast", TimeSlot.MORNING, 2, "restaurant"),
                TemplateRoutine("Morning stretches", TimeSlot.MORNING, 2, "fitness_center"),
                TemplateRoutine("Drink water", TimeSlot.SCHOOL, 1, "water_drop"),
                TemplateRoutine("Eat fruit snack", TimeSlot.SCHOOL, 1, "restaurant"),
                TemplateRoutine("Active play outside", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Wash hands before eating", TimeSlot.AFTERNOON, 1, "wash"),
                TemplateRoutine("Healthy dinner", TimeSlot.EVENING, 2, "dinner_dining"),
                TemplateRoutine("No screen before bed", TimeSlot.EVENING, 2, "phonelink_off"),
                TemplateRoutine("Shower or bath", TimeSlot.BEDTIME, 1, "shower"),
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Deep breaths before sleep", TimeSlot.BEDTIME, 1, "self_improvement"),
                TemplateRoutine("Lights out on time", TimeSlot.BEDTIME, 1, "bedtime")
            )
        ),

        RoutineTemplate(
            id = -12,
            name = "Creative Kid",
            description = "For artistic kids who love drawing, music, and building things",
            category = TemplateCategory.GENERAL,
            iconName = "palette",
            routines = listOf(
                TemplateRoutine("Wake up", TimeSlot.MORNING, 1, "alarm"),
                TemplateRoutine("Breakfast", TimeSlot.MORNING, 1, "restaurant"),
                TemplateRoutine("Free drawing time", TimeSlot.MORNING, 2, "palette"),
                TemplateRoutine("Homework", TimeSlot.SCHOOL, 3, "menu_book"),
                TemplateRoutine("Read for 15 min", TimeSlot.SCHOOL, 2, "auto_stories"),
                TemplateRoutine("Build or craft project", TimeSlot.AFTERNOON, 3, "construction"),
                TemplateRoutine("Outdoor play", TimeSlot.AFTERNOON, 2, "park"),
                TemplateRoutine("Music or singing", TimeSlot.AFTERNOON, 2, "music_note"),
                TemplateRoutine("Dinner", TimeSlot.EVENING, 1, "dinner_dining"),
                TemplateRoutine("Clean up art supplies", TimeSlot.EVENING, 1, "cleaning_services"),
                TemplateRoutine("Brush teeth", TimeSlot.BEDTIME, 1, "brush"),
                TemplateRoutine("Bedtime story", TimeSlot.BEDTIME, 2, "auto_stories")
            )
        )
    )

    /**
     * Default reward milestones.
     */
    data class MilestoneTemplate(
        val name: String,
        val description: String,
        val requiredPoints: Int,
        val iconName: String
    )

    val DEFAULT_MILESTONES = listOf(
        MilestoneTemplate("First Star!", "Earn your first 10 points", 10, "star"),
        MilestoneTemplate("Rising Star", "Reach 50 points", 50, "star_half"),
        MilestoneTemplate("Super Helper", "Reach 100 points", 100, "stars"),
        MilestoneTemplate("Champion", "Reach 250 points", 250, "emoji_events"),
        MilestoneTemplate("Legend", "Reach 500 points", 500, "military_tech")
    )
}
