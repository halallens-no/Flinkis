package com.halallens.flinkis.ui.screens.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiNature
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.halallens.flinkis.domain.model.ThemeType
import com.halallens.flinkis.ui.animation.pressScale
import com.halallens.flinkis.ui.theme.BoyPrimary
import com.halallens.flinkis.ui.theme.GirlPrimary
import com.halallens.flinkis.ui.theme.NeutralPrimary

@Composable
fun ThemeSelectionScreen(
    onThemeSelected: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var cardsVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { cardsVisible = true }

    val card1Alpha by animateFloatAsState(
        targetValue = if (cardsVisible) 1f else 0f,
        animationSpec = tween(350, delayMillis = 100), label = "c1a"
    )
    val card1Offset by animateFloatAsState(
        targetValue = if (cardsVisible) 0f else 60f,
        animationSpec = tween(350, delayMillis = 100), label = "c1o"
    )
    val card2Alpha by animateFloatAsState(
        targetValue = if (cardsVisible) 1f else 0f,
        animationSpec = tween(350, delayMillis = 250), label = "c2a"
    )
    val card2Offset by animateFloatAsState(
        targetValue = if (cardsVisible) 0f else 60f,
        animationSpec = tween(350, delayMillis = 250), label = "c2o"
    )
    val card3Alpha by animateFloatAsState(
        targetValue = if (cardsVisible) 1f else 0f,
        animationSpec = tween(350, delayMillis = 400), label = "c3a"
    )
    val card3Offset by animateFloatAsState(
        targetValue = if (cardsVisible) 0f else 60f,
        animationSpec = tween(350, delayMillis = 400), label = "c3o"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (cardsVisible) 1f else 0f,
        animationSpec = tween(400), label = "ta"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_welcome),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer { alpha = titleAlpha }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_choose_theme),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.graphicsLayer { alpha = titleAlpha }
        )
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ThemeCard(
                label = stringResource(R.string.onboarding_theme_boy),
                icon = Icons.Filled.RocketLaunch,
                color = BoyPrimary,
                onClick = {
                    viewModel.setTheme(ThemeType.BOY)
                    onThemeSelected()
                },
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer { alpha = card1Alpha; translationY = card1Offset }
            )
            Spacer(modifier = Modifier.width(12.dp))
            ThemeCard(
                label = stringResource(R.string.onboarding_theme_girl),
                icon = Icons.Filled.AutoAwesome,
                color = GirlPrimary,
                onClick = {
                    viewModel.setTheme(ThemeType.GIRL)
                    onThemeSelected()
                },
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer { alpha = card2Alpha; translationY = card2Offset }
            )
            Spacer(modifier = Modifier.width(12.dp))
            ThemeCard(
                label = stringResource(R.string.onboarding_theme_neutral),
                icon = Icons.Filled.EmojiNature,
                color = NeutralPrimary,
                onClick = {
                    viewModel.setTheme(ThemeType.NEUTRAL)
                    onThemeSelected()
                },
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer { alpha = card3Alpha; translationY = card3Offset }
            )
        }
    }
}

@Composable
private fun ThemeCard(
    label: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier
            .height(160.dp)
            .pressScale(interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = BorderStroke(2.dp, color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(48.dp),
                tint = color
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}
