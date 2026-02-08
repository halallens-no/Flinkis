package com.halallens.flinkis.ui.screens.onboarding

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.halallens.flinkis.ui.animation.pressScale
import com.halallens.flinkis.util.LocaleHelper

data class Language(
    val code: String,
    val flag: String,
    val nativeName: String
)

private val LANGUAGES = listOf(
    Language("en", "🇬🇧", "English"),
    Language("nb", "🇳🇴", "Norsk"),
    Language("ar", "🇸🇦", "العربية"),
    Language("da", "🇩🇰", "Dansk"),
    Language("de", "🇩🇪", "Deutsch"),
    Language("fr", "🇫🇷", "Français"),
    Language("in", "🇮🇩", "Indonesia"),
    Language("ja", "🇯🇵", "日本語"),
    Language("ko", "🇰🇷", "한국어"),
    Language("ms", "🇲🇾", "Melayu"),
    Language("nl", "🇳🇱", "Nederlands"),
    Language("sv", "🇸🇪", "Svenska"),
    Language("th", "🇹🇭", "ไทย"),
    Language("tr", "🇹🇷", "Türkçe")
)

@Composable
fun LanguageSelectionScreen(
    onLanguageSelected: () -> Unit
) {
    val context = LocalContext.current
    var cardsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { cardsVisible = true }

    val titleAlpha by animateFloatAsState(
        targetValue = if (cardsVisible) 1f else 0f,
        animationSpec = tween(400),
        label = "title_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🌍",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.graphicsLayer { alpha = titleAlpha }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Choose your language",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer { alpha = titleAlpha }
        )
        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(LANGUAGES) { index, language ->
                val cardAlpha by animateFloatAsState(
                    targetValue = if (cardsVisible) 1f else 0f,
                    animationSpec = tween(350, delayMillis = 50 + index * 50),
                    label = "card_${language.code}_alpha"
                )
                val cardOffset by animateFloatAsState(
                    targetValue = if (cardsVisible) 0f else 40f,
                    animationSpec = tween(350, delayMillis = 50 + index * 50),
                    label = "card_${language.code}_offset"
                )

                LanguageCard(
                    language = language,
                    onClick = {
                        LocaleHelper.setLanguage(context, language.code)
                        // Restart activity with fresh task to apply new locale via attachBaseContext
                        // recreate() restores NavController state (loops back here), so use CLEAR_TASK instead
                        val intent = Intent(context, context.javaClass)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    },
                    modifier = Modifier.graphicsLayer {
                        alpha = cardAlpha
                        translationY = cardOffset
                    }
                )
            }
        }
    }
}

@Composable
private fun LanguageCard(
    language: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
            .pressScale(interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = language.flag,
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = language.nativeName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
