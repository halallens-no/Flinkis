package com.halallens.flinkis.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Avatar selection grid with emoji avatars.
 */
@Composable
fun AvatarPicker(
    selectedAvatar: Int,
    onAvatarSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val avatars = listOf(
        "\uD83E\uDD81", // lion
        "\uD83E\uDD84", // unicorn
        "\uD83D\uDC3B", // bear
        "\uD83D\uDC31", // cat
        "\uD83D\uDC36", // dog
        "\uD83E\uDD8B", // butterfly
        "\uD83D\uDC22", // turtle
        "\uD83E\uDD89", // owl
        "\uD83D\uDC2C", // dolphin
        "\uD83D\uDC3C", // panda
        "\uD83E\uDD85", // eagle
        "\uD83D\uDC30"  // rabbit
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(avatars) { index, emoji ->
            Surface(
                onClick = { onAvatarSelected(index) },
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = if (index == selectedAvatar) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                border = if (index == selectedAvatar) {
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                } else null
            ) {
                Text(
                    text = emoji,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.size(56.dp)
                )
            }
        }
    }
}
