package com.halallens.flinkis.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.halallens.flinkis.R
import com.halallens.flinkis.domain.model.TemplateCategory
import com.halallens.flinkis.domain.model.ThemeType
import com.halallens.flinkis.domain.model.TimeSlot

@Composable
fun ThemeType.localizedName(): String = when (this) {
    ThemeType.BOY -> stringResource(R.string.onboarding_theme_boy)
    ThemeType.GIRL -> stringResource(R.string.onboarding_theme_girl)
    ThemeType.NEUTRAL -> stringResource(R.string.onboarding_theme_neutral)
}

@Composable
fun TimeSlot.localizedName(): String = when (this) {
    TimeSlot.MORNING -> stringResource(R.string.timeslot_morning)
    TimeSlot.SCHOOL -> stringResource(R.string.timeslot_school)
    TimeSlot.AFTERNOON -> stringResource(R.string.timeslot_afternoon)
    TimeSlot.EVENING -> stringResource(R.string.timeslot_evening)
    TimeSlot.BEDTIME -> stringResource(R.string.timeslot_bedtime)
}

@Composable
fun TemplateCategory.localizedName(): String = when (this) {
    TemplateCategory.GENERAL -> stringResource(R.string.template_category_general)
    TemplateCategory.ISLAMIC -> stringResource(R.string.template_category_islamic)
    TemplateCategory.CUSTOM -> stringResource(R.string.template_category_custom)
}
