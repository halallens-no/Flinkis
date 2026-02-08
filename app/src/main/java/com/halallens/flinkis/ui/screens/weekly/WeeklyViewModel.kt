package com.halallens.flinkis.ui.screens.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halallens.flinkis.data.repository.ChildRepository
import com.halallens.flinkis.domain.model.DayProgress
import com.halallens.flinkis.domain.model.WeeklyProgress
import com.halallens.flinkis.domain.usecase.GetWeeklyProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeeklyUiState(
    val weeklyProgress: WeeklyProgress? = null,
    val selectedDay: DayProgress? = null,
    val isLoading: Boolean = true,
    val weekOffset: Int = 0
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WeeklyViewModel @Inject constructor(
    private val childRepository: ChildRepository,
    private val getWeeklyProgressUseCase: GetWeeklyProgressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeeklyUiState())
    val uiState: StateFlow<WeeklyUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadWeeklyProgress(0)
    }

    private fun loadWeeklyProgress(weekOffset: Int) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            childRepository.getActiveChild()
                .flatMapLatest { child ->
                    if (child == null) return@flatMapLatest flowOf<WeeklyProgress?>(null)
                    getWeeklyProgressUseCase(child.id, weekOffset)
                }
                .collect { progress ->
                    _uiState.update {
                        it.copy(
                            weeklyProgress = progress,
                            isLoading = false,
                            weekOffset = weekOffset
                        )
                    }
                }
        }
    }

    fun goToPreviousWeek() {
        val newOffset = _uiState.value.weekOffset - 1
        _uiState.update { it.copy(selectedDay = null) }
        loadWeeklyProgress(newOffset)
    }

    fun goToNextWeek() {
        val current = _uiState.value.weekOffset
        if (current < 0) {
            _uiState.update { it.copy(selectedDay = null) }
            loadWeeklyProgress(current + 1)
        }
    }

    fun goToCurrentWeek() {
        _uiState.update { it.copy(selectedDay = null) }
        loadWeeklyProgress(0)
    }

    fun selectDay(day: DayProgress) {
        _uiState.update { it.copy(selectedDay = day) }
    }

    fun clearSelectedDay() {
        _uiState.update { it.copy(selectedDay = null) }
    }
}
