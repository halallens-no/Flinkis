package com.halallens.flinkis.ui.screens.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halallens.flinkis.data.repository.TemplateRepository
import com.halallens.flinkis.domain.model.RoutineTemplate
import com.halallens.flinkis.domain.model.TemplateCategory
import com.halallens.flinkis.domain.model.TemplateRoutine
import com.halallens.flinkis.domain.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SlotSuggestion(
    val name: String,
    val points: Int,
    val iconName: String,
    val isSelected: Boolean = true
)

data class QuickBuilderUiState(
    val templateName: String = "",
    val activeSlots: Set<TimeSlot> = TimeSlot.entries.toSet(),
    val slotSuggestions: Map<TimeSlot, List<SlotSuggestion>> = emptyMap(),
    val isSaving: Boolean = false
)

@HiltViewModel
class QuickBuilderViewModel @Inject constructor(
    private val templateRepository: TemplateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuickBuilderUiState())
    val uiState: StateFlow<QuickBuilderUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(slotSuggestions = defaultSuggestions())
    }

    fun setTemplateName(name: String) {
        _uiState.value = _uiState.value.copy(templateName = name)
    }

    fun toggleSlot(slot: TimeSlot) {
        val current = _uiState.value.activeSlots.toMutableSet()
        if (current.contains(slot)) current.remove(slot) else current.add(slot)
        _uiState.value = _uiState.value.copy(activeSlots = current)
    }

    fun toggleSuggestion(slot: TimeSlot, index: Int) {
        val suggestions = _uiState.value.slotSuggestions.toMutableMap()
        val list = suggestions[slot]?.toMutableList() ?: return
        list[index] = list[index].copy(isSelected = !list[index].isSelected)
        suggestions[slot] = list
        _uiState.value = _uiState.value.copy(slotSuggestions = suggestions)
    }

    fun saveTemplate(onSaved: () -> Unit) {
        val state = _uiState.value
        if (state.templateName.isBlank()) return

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)
            val routines = state.activeSlots.flatMap { slot ->
                state.slotSuggestions[slot]
                    ?.filter { it.isSelected }
                    ?.map { suggestion ->
                        TemplateRoutine(
                            name = suggestion.name,
                            timeSlot = slot,
                            points = suggestion.points,
                            iconName = suggestion.iconName
                        )
                    } ?: emptyList()
            }
            templateRepository.saveTemplate(
                RoutineTemplate(
                    name = state.templateName,
                    description = "Custom template",
                    category = TemplateCategory.CUSTOM,
                    iconName = "star",
                    routines = routines,
                    isBuiltIn = false
                )
            )
            _uiState.value = _uiState.value.copy(isSaving = false)
            onSaved()
        }
    }

    private fun defaultSuggestions(): Map<TimeSlot, List<SlotSuggestion>> = mapOf(
        TimeSlot.MORNING to listOf(
            SlotSuggestion("Wake up", 1, "alarm"),
            SlotSuggestion("Make bed", 1, "bed"),
            SlotSuggestion("Brush teeth", 1, "brush"),
            SlotSuggestion("Get dressed", 1, "checkroom"),
            SlotSuggestion("Eat breakfast", 2, "restaurant"),
            SlotSuggestion("Fajr prayer", 3, "mosque", isSelected = false)
        ),
        TimeSlot.SCHOOL to listOf(
            SlotSuggestion("Pack school bag", 1, "backpack"),
            SlotSuggestion("Do homework", 3, "menu_book"),
            SlotSuggestion("Read for 15 min", 2, "auto_stories"),
            SlotSuggestion("Quran reading", 3, "auto_stories", isSelected = false),
            SlotSuggestion("Dhuhr prayer", 2, "mosque", isSelected = false)
        ),
        TimeSlot.AFTERNOON to listOf(
            SlotSuggestion("Outdoor play", 2, "park"),
            SlotSuggestion("Clean up toys", 1, "toys"),
            SlotSuggestion("Help with chores", 2, "cleaning_services"),
            SlotSuggestion("Asr prayer", 2, "mosque", isSelected = false)
        ),
        TimeSlot.EVENING to listOf(
            SlotSuggestion("Eat dinner", 1, "dinner_dining"),
            SlotSuggestion("Help set table", 1, "table_restaurant"),
            SlotSuggestion("Screen-free time", 2, "phonelink_off"),
            SlotSuggestion("Maghrib prayer", 2, "mosque", isSelected = false),
            SlotSuggestion("Isha prayer", 2, "mosque", isSelected = false)
        ),
        TimeSlot.BEDTIME to listOf(
            SlotSuggestion("Brush teeth", 1, "brush"),
            SlotSuggestion("Put on pajamas", 1, "checkroom"),
            SlotSuggestion("Read bedtime story", 2, "auto_stories"),
            SlotSuggestion("Lights out on time", 1, "bedtime"),
            SlotSuggestion("Dua before sleep", 1, "bedtime", isSelected = false)
        )
    )
}
