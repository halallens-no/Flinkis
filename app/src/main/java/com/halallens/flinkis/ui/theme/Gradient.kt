package com.halallens.flinkis.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

/**
 * Theme-aware gradient brush providers for MyRoutine.
 * All gradients automatically adapt to the active theme (Adventure/Sparkle/Nature)
 * and light/dark mode.
 */
object MyRoutineGradients {

    /** Primary color gradient (primary -> primaryContainer). */
    @Composable
    fun primaryGradient(): Brush {
        val colors = MaterialTheme.colorScheme
        return Brush.horizontalGradient(
            colors = listOf(colors.primary, colors.primaryContainer)
        )
    }

    /** Accent gradient (tertiary -> secondary) for highlights. */
    @Composable
    fun accentGradient(): Brush {
        val colors = MaterialTheme.colorScheme
        return Brush.horizontalGradient(
            colors = listOf(colors.tertiary, colors.secondary)
        )
    }

    /** Subtle vertical surface gradient for card backgrounds. */
    @Composable
    fun surfaceGradient(): Brush {
        val colors = MaterialTheme.colorScheme
        return Brush.verticalGradient(
            colors = listOf(
                colors.surface,
                colors.surfaceVariant.copy(alpha = 0.3f)
            )
        )
    }

    /** Vertical gradient for stat cards (start -> end with alpha). */
    @Composable
    fun statCardGradient(baseColor: androidx.compose.ui.graphics.Color): Brush {
        return Brush.verticalGradient(
            colors = listOf(
                baseColor,
                baseColor.copy(alpha = 0.6f)
            )
        )
    }
}
