package com.halallens.flinkis.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import com.halallens.flinkis.ui.util.localizedName
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.domain.model.TimeSlot

/**
 * Section header for time slot groups with gradient divider
 * and animated icon entrance.
 */
@Composable
fun TimeSlotHeader(
    timeSlot: TimeSlot,
    modifier: Modifier = Modifier
) {
    val icon = when (timeSlot) {
        TimeSlot.MORNING -> Icons.Filled.WbSunny
        TimeSlot.SCHOOL -> Icons.Filled.School
        TimeSlot.AFTERNOON -> Icons.Filled.Cloud
        TimeSlot.EVENING -> Icons.Filled.NightsStay
        TimeSlot.BEDTIME -> Icons.Filled.Bedtime
    }

    // Icon entrance: rotate from -15deg to 0 with spring
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val iconRotation by animateFloatAsState(
        targetValue = if (entered) 0f else -15f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon_rotation"
    )
    val iconAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "icon_alpha"
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = timeSlot.localizedName(),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        rotationZ = iconRotation
                        alpha = iconAlpha
                    },
                tint = primaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = timeSlot.localizedName(),
                style = MaterialTheme.typography.titleMedium,
                color = primaryColor
            )
        }

        // Gradient divider: primary -> transparent
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.dp)
        ) {
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor, Color.Transparent),
                    start = Offset.Zero,
                    end = Offset(size.width, 0f)
                ),
                start = Offset.Zero,
                end = Offset(size.width, 0f),
                strokeWidth = size.height
            )
        }
    }
}
