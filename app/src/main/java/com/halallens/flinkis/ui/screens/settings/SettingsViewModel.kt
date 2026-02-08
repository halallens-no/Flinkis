package com.halallens.flinkis.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.halallens.flinkis.data.preferences.UserPreferences
import com.halallens.flinkis.data.repository.ChildRepository
import com.halallens.flinkis.domain.model.ChildProfile
import com.halallens.flinkis.domain.model.ThemeType
import com.halallens.flinkis.domain.usecase.GeneratePrintableSheetUseCase
import com.halallens.flinkis.util.PrintHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val activeChild: ChildProfile? = null,
    val children: List<ChildProfile> = emptyList(),
    val themeType: ThemeType = ThemeType.NEUTRAL,
    val darkMode: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val childRepository: ChildRepository,
    private val userPreferences: UserPreferences,
    private val generatePrintableSheetUseCase: GeneratePrintableSheetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                childRepository.getAllChildren(),
                childRepository.getActiveChild(),
                userPreferences.themeType,
                userPreferences.darkMode
            ) { children, active, theme, dark ->
                SettingsUiState(
                    activeChild = active,
                    children = children,
                    themeType = theme,
                    darkMode = dark,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }

    fun switchTheme(themeType: ThemeType) {
        viewModelScope.launch {
            userPreferences.setThemeType(themeType)
            // Also persist to active child's profile
            _uiState.value.activeChild?.let { child ->
                childRepository.updateChildTheme(child.id, themeType)
            }
        }
    }

    fun updateChildProfile(name: String, avatarId: Int) {
        viewModelScope.launch {
            _uiState.value.activeChild?.let { child ->
                val updated = child.copy(name = name, avatarId = avatarId)
                childRepository.updateChild(updated)
            }
        }
    }

    fun switchChild(childId: Long) {
        viewModelScope.launch {
            childRepository.switchActiveChild(childId)
            // Sync global theme to the new active child's theme
            val child = childRepository.getActiveChildOnce()
            child?.let { userPreferences.setThemeType(it.themeType) }
        }
    }

    fun printRoutines(context: Context) {
        viewModelScope.launch {
            val child = _uiState.value.activeChild ?: return@launch
            val sheet = generatePrintableSheetUseCase(child.id, child.name)
            PrintHelper.printRoutineSheet(context, sheet, _uiState.value.themeType)
        }
    }
}
