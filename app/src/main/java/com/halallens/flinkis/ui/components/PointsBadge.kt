package com.halallens.flinkis.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.ui.theme.StarGold

/**
 * Star-shaped points badge.
 */
@Composable
fun PointsBadge(
    points: Int,
    isEarned: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isEarned) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = "$points points",
            modifier = Modifier.size(20.dp),
            tint = if (isEarned) StarGold else MaterialTheme.colorScheme.outline
        )
        Text(
            text = "$points",
            style = MaterialTheme.typography.labelMedium,
            color = if (isEarned) StarGold else MaterialTheme.colorScheme.outline
        )
    }
}
