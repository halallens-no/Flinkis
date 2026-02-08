package com.halallens.flinkis.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halallens.flinkis.data.repository.ChildRepository
import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.domain.model.Routine
import com.halallens.flinkis.domain.model.TimeSlot
import com.halallens.flinkis.domain.usecase.ManageRoutinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditRoutineUiState(
    val routines: List<Routine> = emptyList(),
    val activeChildId: Long = 0,
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EditRoutineViewModel @Inject constructor(
    private val childRepository: ChildRepository,
    private val routineRepository: RoutineRepository,
    private val manageRoutinesUseCase: ManageRoutinesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditRoutineUiState())
    val uiState: StateFlow<EditRoutineUiState> = _uiState.asStateFlow()

    init {
        loadRoutines()
    }

    private fun loadRoutines() {
        viewModelScope.launch {
            childRepository.getActiveChild()
                .flatMapLatest { child ->
                    if (child == null) return@flatMapLatest flowOf(emptyList<Routine>() to 0L)
                    routineRepository.getRoutinesForChild(child.id)
                        .flatMapLatest { routines -> flowOf(routines to child.id) }
                }
                .collect { (routines, childId) ->
                    _uiState.update {
                        it.copy(
                            routines = routines,
                            activeChildId = childId,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun addRoutine(name: String, timeSlot: TimeSlot, points: Int) {
        viewModelScope.launch {
            val childId = _uiState.value.activeChildId
            if (childId == 0L) return@launch
            manageRoutinesUseCase.addRoutine(
                Routine(
                    childId = childId,
                    name = name,
                    timeSlot = timeSlot,
                    points = points,
                    isCustom = true
                )
            )
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            manageRoutinesUseCase.deleteRoutine(routine)
        }
    }

    fun toggleRoutineActive(routine: Routine) {
        viewModelScope.launch {
            manageRoutinesUseCase.updateRoutine(routine.copy(isActive = !routine.isActive))
        }
    }
}
