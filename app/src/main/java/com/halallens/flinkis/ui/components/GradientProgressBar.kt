package com.halallens.flinkis.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Rounded progress bar with gradient fill and animated transitions.
 * Replaces the plain LinearProgressIndicator across the app.
 */
@Composable
fun GradientProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 12.dp,
    cornerRadius: Dp = 6.dp,
    brush: Brush? = null,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    animated: Boolean = true
) {
    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val effectiveBrush = brush ?: Brush.horizontalGradient(
        colors = listOf(primary, primaryContainer)
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = if (animated) {
            tween(durationMillis = 600, easing = FastOutSlowInEasing)
        } else {
            tween(durationMillis = 0)
        },
        label = "gradient_progress"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val radius = CornerRadius(cornerRadius.toPx())

        // Track
        drawRoundRect(
            color = trackColor,
            cornerRadius = radius
        )

        // Filled portion
        if (animatedProgress > 0f) {
            drawRoundRect(
                brush = effectiveBrush,
                size = Size(
                    width = size.width * animatedProgress,
                    height = size.height
                ),
                cornerRadius = radius
            )
        }
    }
}
