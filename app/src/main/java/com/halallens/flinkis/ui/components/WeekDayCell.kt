package com.halallens.flinkis.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halallens.flinkis.domain.model.DayProgress
import com.halallens.flinkis.ui.animation.pressScale
import com.halallens.flinkis.ui.theme.CheckGreen
import com.halallens.flinkis.ui.theme.Gray200
import com.halallens.flinkis.ui.theme.StarGold
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekDayCell(
    dayProgress: DayProgress,
    isToday: Boolean,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val date = LocalDate.parse(dayProgress.date, DateTimeFormatter.ISO_LOCAL_DATE)
    val dayLetter = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault())
    val dayNumber = date.dayOfMonth.toString()

    val completionRatio = if (dayProgress.totalCount > 0) {
        dayProgress.completedCount.toFloat() / dayProgress.totalCount
    } else 0f

    val progressColor = when {
        completionRatio >= 1f -> CheckGreen
        completionRatio >= 0.5f -> StarGold
        completionRatio > 0f -> MaterialTheme.colorScheme.primary
        else -> Gray200
    }

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .pressScale(interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Single-letter day name
        Text(
            text = dayLetter,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = if (isToday) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Circle with date number + progress ring
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(40.dp)
        ) {
            if (isToday || isSelected) {
                // Filled circle background for today/selected
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(
                        alpha = if (isSelected) 0.2f else 0.12f
                    )
                ) {}
            }

            // Progress ring
            Canvas(modifier = Modifier.size(36.dp)) {
                // Track ring (always visible)
                drawCircle(
                    color = Gray200,
                    style = Stroke(width = 3.dp.toPx())
                )
                // Progress arc
                if (completionRatio > 0f) {
                    drawArc(
                        color = progressColor,
                        startAngle = -90f,
                        sweepAngle = 360f * completionRatio,
                        useCenter = false,
                        style = Stroke(
                            width = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }

            // Date number
            Text(
                text = dayNumber,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp,
                color = if (isToday) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
