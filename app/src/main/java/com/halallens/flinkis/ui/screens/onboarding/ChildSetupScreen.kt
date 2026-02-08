package com.halallens.flinkis.ui.screens.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.halallens.flinkis.domain.model.ThemeType
import com.halallens.flinkis.ui.animation.pressScale
import com.halallens.flinkis.ui.components.AvatarPicker

@Composable
fun ChildSetupScreen(
    onSetupComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val themeType by viewModel.themeType.collectAsState(initial = ThemeType.NEUTRAL)
    var childName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableIntStateOf(0) }

    // Sequential entrance
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val titleAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(350, delayMillis = 0), label = "ta"
    )
    val titleOffset by animateFloatAsState(
        targetValue = if (visible) 0f else 30f,
        animationSpec = tween(350, delayMillis = 0), label = "to"
    )
    val fieldAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(350, delayMillis = 150), label = "fa"
    )
    val fieldOffset by animateFloatAsState(
        targetValue = if (visible) 0f else 30f,
        animationSpec = tween(350, delayMillis = 150), label = "fo"
    )
    val avatarAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(350, delayMillis = 300), label = "aa"
    )
    val avatarOffset by animateFloatAsState(
        targetValue = if (visible) 0f else 30f,
        animationSpec = tween(350, delayMillis = 300), label = "ao"
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(350, delayMillis = 450), label = "ba"
    )
    val buttonOffset by animateFloatAsState(
        targetValue = if (visible) 0f else 20f,
        animationSpec = tween(350, delayMillis = 450), label = "bo"
    )

    val buttonInteraction = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_whats_your_name),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer {
                alpha = titleAlpha; translationY = titleOffset
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = childName,
            onValueChange = { childName = it },
            label = { Text(stringResource(R.string.your_name)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .graphicsLayer { alpha = fieldAlpha; translationY = fieldOffset }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.onboarding_pick_avatar),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.graphicsLayer {
                alpha = avatarAlpha; translationY = avatarOffset
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        AvatarPicker(
            selectedAvatar = selectedAvatar,
            onAvatarSelected = { selectedAvatar = it },
            modifier = Modifier.graphicsLayer {
                alpha = avatarAlpha; translationY = avatarOffset
            }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (childName.isNotBlank()) {
                    viewModel.createChild(
                        name = childName.trim(),
                        avatarId = selectedAvatar,
                        themeType = themeType,
                        onComplete = onSetupComplete
                    )
                }
            },
            enabled = childName.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .pressScale(buttonInteraction)
                .graphicsLayer {
                    alpha = buttonAlpha; translationY = buttonOffset
                }
        ) {
            Text(
                text = stringResource(R.string.onboarding_lets_go),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
