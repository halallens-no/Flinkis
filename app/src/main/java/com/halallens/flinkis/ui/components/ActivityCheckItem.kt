package com.halallens.flinkis.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.R
import com.halallens.flinkis.ui.animation.CompletionCelebration
import com.halallens.flinkis.ui.animation.pressScale
import com.halallens.flinkis.ui.theme.CheckGreen
import com.halallens.flinkis.ui.theme.Gray400

/**
 * Animated activity row with card elevation, custom checkmark,
 * confetti celebration on completion, and press-scale feedback.
 */
@Composable
fun ActivityCheckItem(
    routineName: String,
    points: Int,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Track completions for confetti trigger
    var celebrationCount by remember { mutableIntStateOf(0) }
    var wasCompleted by remember { mutableStateOf(isCompleted) }
    LaunchedEffect(isCompleted) {
        if (isCompleted && !wasCompleted) {
            celebrationCount++
        }
        wasCompleted = isCompleted
    }

    // Animated checkmark scale
    val checkScale by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "check_scale"
    )

    // Points badge bounce
    val pointsScale by animateFloatAsState(
        targetValue = if (isCompleted) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "points_bounce"
    )

    Box {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .pressScale(interactionSource)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onToggle
                ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp,
                pressedElevation = 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isCompleted) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Custom animated checkmark circle
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isCompleted) CheckGreen
                            else Gray400.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(if (isCompleted) R.string.a11y_completed else R.string.a11y_not_completed),
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer {
                                scaleX = checkScale
                                scaleY = checkScale
                                alpha = checkScale
                            },
                        tint = MaterialTheme.colorScheme.surface
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = routineName,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                Box(modifier = Modifier.graphicsLayer {
                    scaleX = pointsScale
                    scaleY = pointsScale
                }) {
                    PointsBadge(points = points, isEarned = isCompleted)
                }
            }
        }

        // Confetti overlay
        CompletionCelebration(
            trigger = celebrationCount,
            modifier = Modifier.matchParentSize()
        )
    }
}
