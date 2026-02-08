package com.halallens.flinkis.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halallens.flinkis.data.repository.ActivityLogRepository
import com.halallens.flinkis.data.repository.ChildRepository
import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.domain.model.Routine
import com.halallens.flinkis.domain.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class RoutineWithStatus(
    val routine: Routine,
    val isCompleted: Boolean
)

data class TodayUiState(
    val routinesByTimeSlot: Map<TimeSlot, List<RoutineWithStatus>> = emptyMap(),
    val completedCount: Int = 0,
    val totalRoutines: Int = 0,
    val progress: Float = 0f,
    val todayPoints: Int = 0,
    val selectedDate: LocalDate = LocalDate.now(),
    val isToday: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val childRepository: ChildRepository,
    private val routineRepository: RoutineRepository,
    private val activityLogRepository: ActivityLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadRoutinesForDate(LocalDate.now())
    }

    private fun loadRoutinesForDate(date: LocalDate) {
        loadJob?.cancel()
        val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val dayOfWeek = date.dayOfWeek.value

        loadJob = viewModelScope.launch {
            childRepository.getActiveChild()
                .flatMapLatest { child ->
                    if (child == null) return@flatMapLatest flowOf(
                        TodayUiState(selectedDate = date, isToday = date == LocalDate.now())
                    )
                    combine(
                        routineRepository.getRoutinesForDay(child.id, dayOfWeek),
                        activityLogRepository.getLogsForDate(child.id, dateStr)
                    ) { routines, logs ->
                        val completedRoutineIds = logs.filter { it.completed }.map { it.routineId }.toSet()
                        val routinesWithStatus = routines.map { routine ->
                            RoutineWithStatus(
                                routine = routine,
                                isCompleted = completedRoutineIds.contains(routine.id)
                            )
                        }
                        val byTimeSlot = routinesWithStatus.groupBy { it.routine.timeSlot }
                        val completedCount = routinesWithStatus.count { it.isCompleted }
                        val total = routinesWithStatus.size

                        TodayUiState(
                            routinesByTimeSlot = byTimeSlot,
                            completedCount = completedCount,
                            totalRoutines = total,
                            progress = if (total > 0) completedCount.toFloat() / total else 0f,
                            todayPoints = logs.filter { it.completed }.sumOf { it.pointsEarned },
                            selectedDate = date,
                            isToday = date == LocalDate.now()
                        )
                    }
                }
                .collect { state -> _uiState.update { state } }
        }
    }

    fun goToPreviousDay() {
        loadRoutinesForDate(_uiState.value.selectedDate.minusDays(1))
    }

    fun goToNextDay() {
        val current = _uiState.value.selectedDate
        if (current < LocalDate.now()) {
            loadRoutinesForDate(current.plusDays(1))
        }
    }

    fun goToToday() {
        loadRoutinesForDate(LocalDate.now())
    }

    fun toggleRoutine(routine: Routine) {
        val dateStr = _uiState.value.selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        viewModelScope.launch {
            childRepository.getActiveChild().collect { child ->
                child?.let {
                    activityLogRepository.toggleCompletion(
                        routineId = routine.id,
                        childId = it.id,
                        date = dateStr,
                        points = routine.points
                    )
                }
                return@collect
            }
        }
    }
}
