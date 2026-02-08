package com.halallens.flinkis.ui.components

import android.app.Activity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptiveLayout(
    mainContent: @Composable (Modifier) -> Unit,
    detailContent: (@Composable (Modifier) -> Unit)? = null
) {
    val activity = LocalContext.current as? Activity
    val windowSizeClass = activity?.let { calculateWindowSizeClass(it) }

    val isExpanded = windowSizeClass?.widthSizeClass == WindowWidthSizeClass.Expanded

    if (isExpanded && detailContent != null) {
        Row(modifier = Modifier.fillMaxSize()) {
            mainContent(Modifier.weight(0.4f))
            detailContent(Modifier.weight(0.6f))
        }
    } else {
        mainContent(Modifier.fillMaxSize())
    }
}
