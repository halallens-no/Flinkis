package com.halallens.flinkis.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.ui.animation.pressScale

/**
 * Pill-shaped button with press-scale feedback.
 *
 * Two visual variants:
 * - **Solid** (default): primary colour fill.
 * - **Outlined** ([outlined] = true): near-transparent fill with onSurface text.
 */
@Composable
fun GlassPillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    outlined: Boolean = false,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = onClick,
        modifier = modifier.pressScale(interactionSource),
        enabled = enabled,
        shape = RoundedCornerShape(28.dp),
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 14.dp),
        colors = if (outlined) {
            ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White.copy(alpha = 0.08f),
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        interactionSource = interactionSource,
        content = { content() }
    )
}
