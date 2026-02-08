package com.halallens.flinkis.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.ui.screens.rewards.MilestoneDisplay
import com.halallens.flinkis.ui.theme.Gray400
import com.halallens.flinkis.ui.theme.StarGold

@Composable
fun MilestoneCard(
    milestone: MilestoneDisplay,
    modifier: Modifier = Modifier
) {
    val icon = when (milestone.iconName) {
        "star" -> Icons.Filled.Star
        "star_half" -> Icons.Filled.StarHalf
        "stars" -> Icons.Filled.Stars
        "emoji_events" -> Icons.Filled.EmojiEvents
        "diamond" -> Icons.Filled.Diamond
        else -> Icons.Filled.Star
    }

    // Subtle pulsing scale for earned icon
    val pulseTransition = rememberInfiniteTransition(label = "milestone_pulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (milestone.isEarned) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_pulse"
    )

    // Animated icon tint alpha (earned state transition)
    val iconAlpha by animateFloatAsState(
        targetValue = if (milestone.isEarned) 1f else 0.5f,
        animationSpec = tween(400),
        label = "icon_alpha"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (milestone.isEarned) 3.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (milestone.isEarned) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = milestone.name,
                modifier = Modifier
                    .size(40.dp)
                    .graphicsLayer {
                        scaleX = pulseScale
                        scaleY = pulseScale
                        alpha = iconAlpha
                    },
                tint = if (milestone.isEarned) StarGold else Gray400
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = milestone.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = milestone.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                GradientProgressBar(
                    progress = milestone.progress,
                    modifier = Modifier.fillMaxWidth(),
                    height = 8.dp,
                    cornerRadius = 4.dp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${milestone.requiredPoints}",
                style = MaterialTheme.typography.labelLarge,
                color = if (milestone.isEarned) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Gray400
                }
            )
        }
    }
}
