package com.halallens.flinkis.ui.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Shared animation specifications and modifiers for MyRoutine.
 */
object MyRoutineAnimations {
    /** Stagger delay between list items in milliseconds. */
    const val STAGGER_DELAY_MS = 50

    /** Material Design 3 emphasized decelerate. */
    val enterEasing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)

    /** Material Design 3 emphasized accelerate. */
    val exitEasing = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)

    /** Bouncy spring for playful feedback. */
    fun <T> bouncySpring() = spring<T>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    /** Snappy spring for quick, non-bouncy transitions. */
    fun <T> snappySpring() = spring<T>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
}

/**
 * Shrinks the element to 96% when pressed, with a bouncy spring release.
 * Attach a [MutableInteractionSource] to both this modifier and the clickable.
 */
fun Modifier.pressScale(interactionSource: MutableInteractionSource): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "press_scale"
    )
    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/**
 * Staggered slide-in entrance for lazy list items.
 * Each item slides up from [slideDistance] dp and fades in,
 * delayed by [index] * [MyRoutineAnimations.STAGGER_DELAY_MS].
 */
fun Modifier.staggeredSlideIn(
    index: Int,
    visible: Boolean,
    slideDistance: Float = 40f
): Modifier = composed {
    val delay = index * MyRoutineAnimations.STAGGER_DELAY_MS
    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else slideDistance,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = delay,
            easing = MyRoutineAnimations.enterEasing
        ),
        label = "stagger_offset_$index"
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            delayMillis = delay
        ),
        label = "stagger_alpha_$index"
    )
    this.graphicsLayer {
        translationY = offsetY
        this.alpha = alpha
    }
}
