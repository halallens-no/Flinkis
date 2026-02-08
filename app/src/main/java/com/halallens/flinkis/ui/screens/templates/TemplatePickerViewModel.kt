package com.halallens.flinkis.ui.screens.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halallens.flinkis.data.repository.ChildRepository
import com.halallens.flinkis.data.repository.TemplateRepository
import com.halallens.flinkis.domain.model.RoutineTemplate
import com.halallens.flinkis.domain.model.TemplateCategory
import com.halallens.flinkis.domain.model.TemplateRoutine
import com.halallens.flinkis.domain.model.TimeSlot
import com.halallens.flinkis.domain.usecase.ApplyTemplateUseCase
import com.halallens.flinkis.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditableRoutine(
    val name: String,
    val timeSlot: TimeSlot,
    val points: Int,
    val iconName: String,
    val daysOfWeek: List<Int>,
    val isEnabled: Boolean = true
)

data class TemplatePickerUiState(
    val allTemplates: List<RoutineTemplate> = emptyList(),
    val selectedCategory: TemplateCategory = TemplateCategory.GENERAL,
    val selectedTemplate: RoutineTemplate? = null,
    val diff: ApplyTemplateUseCase.TemplateDiff? = null,
    val showConfirmation: Boolean = false,
    val isApplying: Boolean = false,
    val activeChildId: Long = 0,
    val editableRoutines: List<EditableRoutine> = emptyList()
)

@HiltViewModel
class TemplatePickerViewModel @Inject constructor(
    private val applyTemplateUseCase: ApplyTemplateUseCase,
    private val templateRepository: TemplateRepository,
    private val childRepository: ChildRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TemplatePickerUiState())
    val uiState: StateFlow<TemplatePickerUiState> = _uiState.asStateFlow()

    init {
        loadTemplates()
    }

    private fun loadTemplates() {
        viewModelScope.launch {
            childRepository.getActiveChild().collect { child ->
                child?.let {
                    val builtIn = Constants.BUILT_IN_TEMPLATES
                    templateRepository.getCustomTemplates().collect { custom ->
                        _uiState.value = _uiState.value.copy(
                            allTemplates = builtIn + custom,
                            activeChildId = it.id
                        )
                    }
                }
            }
        }
    }

    fun selectCategory(category: TemplateCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun selectTemplate(template: RoutineTemplate) {
        viewModelScope.launch {
            val diff = applyTemplateUseCase.previewDiff(
                template = template,
                childId = _uiState.value.activeChildId
            )
            val editable = template.routines.map { tr ->
                EditableRoutine(
                    name = tr.name,
                    timeSlot = tr.timeSlot,
                    points = tr.points,
                    iconName = tr.iconName,
                    daysOfWeek = tr.daysOfWeek,
                    isEnabled = true
                )
            }
            _uiState.value = _uiState.value.copy(
                selectedTemplate = template,
                diff = diff,
                editableRoutines = editable,
                showConfirmation = true
            )
        }
    }

    fun dismissConfirmation() {
        _uiState.value = _uiState.value.copy(
            showConfirmation = false,
            diff = null,
            editableRoutines = emptyList()
        )
    }

    fun toggleRoutine(index: Int) {
        val current = _uiState.value.editableRoutines.toMutableList()
        if (index in current.indices) {
            current[index] = current[index].copy(isEnabled = !current[index].isEnabled)
            _uiState.value = _uiState.value.copy(editableRoutines = current)
        }
    }

    fun addRoutine(name: String, timeSlot: TimeSlot, points: Int) {
        val newRoutine = EditableRoutine(
            name = name,
            timeSlot = timeSlot,
            points = points,
            iconName = timeSlot.iconName,
            daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7),
            isEnabled = true
        )
        val current = _uiState.value.editableRoutines + newRoutine
        _uiState.value = _uiState.value.copy(editableRoutines = current)
    }

    fun confirmApply(onComplete: () -> Unit) {
        val template = _uiState.value.selectedTemplate ?: return
        val childId = _uiState.value.activeChildId
        val enabledRoutines = _uiState.value.editableRoutines.filter { it.isEnabled }

        // Build a modified template with only enabled routines
        val editedTemplate = template.copy(
            routines = enabledRoutines.map { er ->
                TemplateRoutine(
                    name = er.name,
                    timeSlot = er.timeSlot,
                    points = er.points,
                    iconName = er.iconName,
                    daysOfWeek = er.daysOfWeek
                )
            }
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isApplying = true)
            applyTemplateUseCase.apply(editedTemplate, childId)
            _uiState.value = _uiState.value.copy(isApplying = false, showConfirmation = false)
            onComplete()
        }
    }
}
