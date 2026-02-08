package com.halallens.flinkis.ui.screens.weekly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.halallens.flinkis.R
import com.halallens.flinkis.ui.animation.staggeredSlideIn
import com.halallens.flinkis.ui.components.GlassCard
import com.halallens.flinkis.ui.components.GradientProgressBar
import com.halallens.flinkis.ui.components.WeekDayCell
import com.halallens.flinkis.ui.theme.CheckGreen
import com.halallens.flinkis.ui.theme.Gray400
import com.halallens.flinkis.util.DateUtils

@Composable
fun WeeklyScreen(
    viewModel: WeeklyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val todayStr = DateUtils.todayString()
    var cellsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { cellsVisible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
        ) {
            // Header with week navigation
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { viewModel.goToPreviousWeek() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(R.string.a11y_previous_week),
                            modifier = Modifier.padding(6.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (uiState.weekOffset == 0) stringResource(R.string.weekly_this_week)
                                   else if (uiState.weekOffset == -1) stringResource(R.string.weekly_last_week)
                                   else stringResource(R.string.weekly_progress),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        uiState.weeklyProgress?.let { p ->
                            Text(
                                text = "${DateUtils.formatDisplayDate(p.weekStartDate)} — ${DateUtils.formatDisplayDate(p.weekEndDate)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    val canGoForward = uiState.weekOffset < 0
                    Surface(
                        shape = CircleShape,
                        color = if (canGoForward) MaterialTheme.colorScheme.primaryContainer
                               else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(36.dp)
                            .alpha(if (canGoForward) 1f else 0.4f)
                            .clickable(enabled = canGoForward) { viewModel.goToNextWeek() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.a11y_next_week),
                            modifier = Modifier.padding(6.dp),
                            tint = if (canGoForward) MaterialTheme.colorScheme.onPrimaryContainer
                                   else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // "Back to this week" pill
                if (uiState.weekOffset != 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            modifier = Modifier.clickable { viewModel.goToCurrentWeek() }
                        ) {
                            Text(
                                text = stringResource(R.string.weekly_back_to_week),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            uiState.weeklyProgress?.let { progress ->
                // Compact stats card — percentage + progress bar in one row
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${(progress.completionPercentage * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.weekly_completed_of, progress.totalCompleted, progress.totalRoutines),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                GradientProgressBar(
                                    progress = progress.completionPercentage,
                                    modifier = Modifier.fillMaxWidth(),
                                    height = 8.dp,
                                    cornerRadius = 4.dp
                                )
                            }
                        }
                    }
                }

                // Week day selector — all 7 days in one GlassCard
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        fillAlpha = 0.08f
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            progress.dailyProgress.forEachIndexed { index, dayProgress ->
                                WeekDayCell(
                                    dayProgress = dayProgress,
                                    isToday = dayProgress.date == todayStr,
                                    isSelected = uiState.selectedDay?.date == dayProgress.date,
                                    onClick = { viewModel.selectDay(dayProgress) },
                                    modifier = Modifier.staggeredSlideIn(index, cellsVisible)
                                )
                            }
                        }
                    }
                }

                // Day detail section
                val displayDay = uiState.selectedDay
                    ?: progress.dailyProgress.find { it.date == todayStr }

                displayDay?.let { day ->
                    val completedCount = day.routineStatuses.count { it.completed }
                    val totalCount = day.routineStatuses.size

                    // Date header with completion count
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = DateUtils.formatDisplayDate(day.date),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "$completedCount/$totalCount",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (completedCount == totalCount && totalCount > 0)
                                    CheckGreen else MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // All routines grouped in one card
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            fillAlpha = 0.06f
                        ) {
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                day.routineStatuses.forEachIndexed { index, status ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (status.completed)
                                                Icons.Filled.CheckCircle
                                            else
                                                Icons.Outlined.RadioButtonUnchecked,
                                            contentDescription = null,
                                            tint = if (status.completed) CheckGreen else Gray400,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Text(
                                            text = status.routineName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontSize = 15.sp,
                                            modifier = Modifier.weight(1f),
                                            color = if (status.completed)
                                                MaterialTheme.colorScheme.onSurface
                                            else
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    // Subtle divider between items
                                    if (index < day.routineStatuses.lastIndex) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 50.dp, end = 16.dp)
                                                .height(0.5.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.outlineVariant
                                                        .copy(alpha = 0.3f)
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                item {
                    Text(
                        text = stringResource(R.string.loading),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
