package com.halallens.flinkis.ui.screens.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halallens.flinkis.data.repository.ChildRepository
import com.halallens.flinkis.domain.usecase.CalculateRewardPointsUseCase
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

data class MilestoneDisplay(
    val name: String,
    val description: String,
    val requiredPoints: Int,
    val iconName: String,
    val isEarned: Boolean,
    val progress: Float
)

data class RewardsUiState(
    val totalPoints: Int = 0,
    val currentStreak: Int = 0,
    val milestones: List<MilestoneDisplay> = emptyList(),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RewardsViewModel @Inject constructor(
    private val childRepository: ChildRepository,
    private val calculateRewardPointsUseCase: CalculateRewardPointsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RewardsUiState())
    val uiState: StateFlow<RewardsUiState> = _uiState.asStateFlow()

    init {
        loadRewards()
    }

    private fun loadRewards() {
        viewModelScope.launch {
            childRepository.getActiveChild()
                .flatMapLatest { child ->
                    if (child == null) return@flatMapLatest flowOf(-1 to 0)
                    calculateRewardPointsUseCase.getTotalPoints(child.id)
                        .flatMapLatest { points ->
                            val streak = calculateRewardPointsUseCase.calculateStreak(child.id)
                            flowOf(points to streak)
                        }
                }
                .collect { (points, streak) ->
                    if (points < 0) return@collect
                    val milestones = buildMilestoneDisplays(points)
                    _uiState.update {
                        it.copy(
                            totalPoints = points,
                            currentStreak = streak,
                            milestones = milestones,
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun buildMilestoneDisplays(totalPoints: Int): List<MilestoneDisplay> {
        val milestoneData = listOf(
            Triple("First Star", "Earn your first 10 points!", 10),
            Triple("Rising Star", "Reach 50 points!", 50),
            Triple("Super Helper", "Amazing! 100 points!", 100),
            Triple("Champion", "Incredible! 250 points!", 250),
            Triple("Legend", "Legendary! 500 points!", 500)
        )
        val icons = listOf("star", "star_half", "stars", "emoji_events", "diamond")

        return milestoneData.mapIndexed { index, (name, desc, required) ->
            MilestoneDisplay(
                name = name,
                description = desc,
                requiredPoints = required,
                iconName = icons[index],
                isEarned = totalPoints >= required,
                progress = (totalPoints.toFloat() / required).coerceAtMost(1f)
            )
        }
    }
}
