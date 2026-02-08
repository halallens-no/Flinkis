package com.halallens.flinkis.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halallens.flinkis.data.repository.ChildRepository
import com.halallens.flinkis.domain.model.ChildProfile
import com.halallens.flinkis.domain.model.ThemeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ManageChildrenUiState(
    val children: List<ChildProfile> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ManageChildrenViewModel @Inject constructor(
    private val childRepository: ChildRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageChildrenUiState())
    val uiState: StateFlow<ManageChildrenUiState> = _uiState.asStateFlow()

    init {
        loadChildren()
    }

    private fun loadChildren() {
        viewModelScope.launch {
            childRepository.getAllChildren().collect { children ->
                _uiState.update {
                    it.copy(children = children, isLoading = false)
                }
            }
        }
    }

    fun addChild(name: String, avatarId: Int, themeType: ThemeType, onChildCreated: () -> Unit = {}) {
        viewModelScope.launch {
            val childId = childRepository.createChild(
                ChildProfile(
                    name = name,
                    avatarId = avatarId,
                    themeType = themeType,
                    isActive = false
                )
            )
            // Activate the new child so template picker works for them
            childRepository.switchActiveChild(childId)
            onChildCreated()
        }
    }

    fun switchToChild(childId: Long) {
        viewModelScope.launch {
            childRepository.switchActiveChild(childId)
        }
    }
}
