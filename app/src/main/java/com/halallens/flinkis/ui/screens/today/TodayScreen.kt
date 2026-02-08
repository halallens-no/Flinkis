package com.halallens.flinkis.ui.screens.today

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.halallens.flinkis.R
import com.halallens.flinkis.domain.model.TimeSlot
import com.halallens.flinkis.ui.animation.staggeredSlideIn
import com.halallens.flinkis.ui.components.ActivityCheckItem
import com.halallens.flinkis.ui.components.GradientProgressBar
import com.halallens.flinkis.ui.components.TimeSlotHeader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TodayScreen(
    viewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var listVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { listVisible = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Date navigation row
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
                    .clickable { viewModel.goToPreviousDay() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.a11y_previous_day),
                    modifier = Modifier.padding(6.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatDateHeader(uiState.selectedDate, uiState.isToday),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (!uiState.isToday) {
                    Text(
                        text = uiState.selectedDate.format(DateTimeFormatter.ofPattern("EEE, MMM d")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val canGoForward = uiState.selectedDate < LocalDate.now()
            Surface(
                shape = CircleShape,
                color = if (canGoForward) MaterialTheme.colorScheme.primaryContainer
                       else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .size(36.dp)
                    .alpha(if (canGoForward) 1f else 0.4f)
                    .clickable(enabled = canGoForward) { viewModel.goToNextDay() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.a11y_next_day),
                    modifier = Modifier.padding(6.dp),
                    tint = if (canGoForward) MaterialTheme.colorScheme.onPrimaryContainer
                           else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // "Back to today" pill when viewing a past date
        if (!uiState.isToday) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier.clickable { viewModel.goToToday() }
                ) {
                    Text(
                        text = stringResource(R.string.today_back_to_today),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        if (uiState.totalRoutines > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.today_progress, uiState.completedCount, uiState.totalRoutines),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            GradientProgressBar(
                progress = uiState.progress,
                modifier = Modifier.fillMaxWidth(),
                height = 10.dp,
                cornerRadius = 5.dp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Build a flat list of items (headers + routines) for stagger indexing
        val flatItems = remember(uiState.routinesByTimeSlot) {
            buildList {
                TimeSlot.entries.forEach { timeSlot ->
                    val routinesInSlot = uiState.routinesByTimeSlot[timeSlot] ?: emptyList()
                    if (routinesInSlot.isNotEmpty()) {
                        add(FlatItem.Header(timeSlot))
                        routinesInSlot.forEach { add(FlatItem.Routine(it)) }
                    }
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(
                items = flatItems,
                key = { _, item ->
                    when (item) {
                        is FlatItem.Header -> "header_${item.timeSlot.name}"
                        is FlatItem.Routine -> "routine_${item.routineWithStatus.routine.id}"
                    }
                }
            ) { index, item ->
                when (item) {
                    is FlatItem.Header -> {
                        TimeSlotHeader(
                            timeSlot = item.timeSlot,
                            modifier = Modifier.staggeredSlideIn(index, listVisible)
                        )
                    }
                    is FlatItem.Routine -> {
                        ActivityCheckItem(
                            routineName = item.routineWithStatus.routine.name,
                            points = item.routineWithStatus.routine.points,
                            isCompleted = item.routineWithStatus.isCompleted,
                            onToggle = { viewModel.toggleRoutine(item.routineWithStatus.routine) },
                            modifier = Modifier.staggeredSlideIn(index, listVisible)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun formatDateHeader(date: LocalDate, isToday: Boolean): String {
    if (isToday) return stringResource(R.string.today_title)
    val today = LocalDate.now()
    return when {
        date == today.minusDays(1) -> stringResource(R.string.today_yesterday)
        date == today.plusDays(1) -> stringResource(R.string.today_tomorrow)
        else -> date.format(DateTimeFormatter.ofPattern("EEEE"))
    }
}

/** Sealed class for flat list rendering with staggered indices. */
private sealed class FlatItem {
    data class Header(val timeSlot: TimeSlot) : FlatItem()
    data class Routine(val routineWithStatus: RoutineWithStatus) : FlatItem()
}
