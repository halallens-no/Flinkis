package com.halallens.flinkis.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.ui.theme.CheckGreen
import com.halallens.flinkis.ui.theme.PointsPurple
import com.halallens.flinkis.ui.theme.StarGold
import com.halallens.flinkis.ui.theme.StreakFire
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Particle(
    val angle: Float,
    val speed: Float,
    val radius: Float,
    val color: Color
)

/**
 * Canvas-based confetti burst triggered by [trigger] changing to a new positive value.
 * Draws [particleCount] colored circles that burst outward and fade over 800ms.
 */
@Composable
fun CompletionCelebration(
    trigger: Int,
    modifier: Modifier = Modifier,
    particleCount: Int = 12
) {
    val primary = MaterialTheme.colorScheme.primary
    val colors = remember(primary) {
        listOf(StarGold, CheckGreen, primary, PointsPurple, StreakFire)
    }

    val particles = remember(trigger) {
        if (trigger <= 0) emptyList()
        else List(particleCount) {
            Particle(
                angle = Random.nextFloat() * 360f,
                speed = 80f + Random.nextFloat() * 120f,
                radius = 4f + Random.nextFloat() * 6f,
                color = colors[it % colors.size]
            )
        }
    }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger > 0) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
        }
    }

    if (particles.isNotEmpty()) {
        Canvas(modifier = modifier.fillMaxWidth().height(80.dp)) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val t = progress.value
            val alpha = (1f - t).coerceIn(0f, 1f)

            particles.forEach { p ->
                val radians = Math.toRadians(p.angle.toDouble()).toFloat()
                val distance = p.speed * t
                val x = centerX + cos(radians) * distance
                val y = centerY + sin(radians) * distance - (30f * t) // slight upward drift
                drawCircle(
                    color = p.color.copy(alpha = alpha),
                    radius = p.radius * (1f - t * 0.5f),
                    center = Offset(x, y)
                )
            }
        }
    }
}

/**
 * Golden overlay that appears when a milestone is unlocked.
 * Shows a scaling star icon and milestone name, auto-dismisses after 2 seconds.
 */
@Composable
fun MilestoneUnlockedOverlay(
    visible: Boolean,
    milestoneName: String,
    onDismiss: () -> Unit
) {
    LaunchedEffect(visible) {
        if (visible) {
            delay(2000)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Milestone unlocked",
                    modifier = Modifier.size(72.dp),
                    tint = StarGold
                )
                Text(
                    text = milestoneName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text(
                    text = "Milestone Unlocked!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
