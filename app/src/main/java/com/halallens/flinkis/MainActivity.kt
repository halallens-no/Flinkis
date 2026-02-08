package com.halallens.flinkis

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.halallens.flinkis.domain.model.ThemeType
import com.halallens.flinkis.ui.navigation.MyRoutineNavHost
import com.halallens.flinkis.ui.screens.onboarding.OnboardingViewModel
import com.halallens.flinkis.ui.theme.MyRoutineTheme
import com.halallens.flinkis.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - Single activity host for the MyRoutine app
 *
 * Uses Jetpack Compose Navigation with bottom navigation bar.
 * Theme is dynamically selected based on child's preference (Boy/Girl/Neutral).
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Apply locale via a wrapper that overrides resources but preserves the original
        // context chain for getSystemService(). createConfigurationContext() breaks the
        // Activity identity chain, causing PrintManager.print() to crash with
        // "Can print only from an activity". This wrapper keeps system services routed
        // through the original ContextImpl (which has the Activity as outer context).
        val languageCode = LocaleHelper.getLanguage(newBase)
        val locale = java.util.Locale(languageCode)
        java.util.Locale.setDefault(locale)

        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        val localeContext = newBase.createConfigurationContext(config)

        super.attachBaseContext(object : ContextWrapper(newBase) {
            override fun getResources(): Resources = localeContext.resources
            override fun getAssets(): AssetManager = localeContext.assets
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val onboardingViewModel: OnboardingViewModel = hiltViewModel()
            val themeType by onboardingViewModel.themeType.collectAsState(initial = ThemeType.NEUTRAL)

            MyRoutineTheme(themeType = themeType) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MyRoutineNavHost(navController = navController)
                }
            }
        }
    }
}
