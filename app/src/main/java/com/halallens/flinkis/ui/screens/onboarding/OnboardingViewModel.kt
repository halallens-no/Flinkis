package com.halallens.flinkis.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.halallens.flinkis.data.preferences.UserPreferences
import com.halallens.flinkis.data.repository.ChildRepository
import com.halallens.flinkis.domain.model.ChildProfile
import com.halallens.flinkis.domain.model.ThemeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val childRepository: ChildRepository
) : ViewModel() {

    val themeType: Flow<ThemeType> = userPreferences.themeType
    val onboardingCompleted: Flow<Boolean> = userPreferences.onboardingCompleted

    fun setTheme(themeType: ThemeType) {
        viewModelScope.launch {
            userPreferences.setThemeType(themeType)
        }
    }

    fun createChild(name: String, avatarId: Int, themeType: ThemeType, onComplete: () -> Unit) {
        viewModelScope.launch {
            childRepository.createChild(
                ChildProfile(
                    name = name,
                    avatarId = avatarId,
                    themeType = themeType
                )
            )
            userPreferences.setOnboardingCompleted(true)
            onComplete()
        }
    }
}
