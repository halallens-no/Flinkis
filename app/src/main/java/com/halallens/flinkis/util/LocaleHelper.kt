package com.halallens.flinkis.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {
    private const val PREFS_NAME = "flinkis_prefs"
    private const val KEY_LANGUAGE = "app_language"

    // Supported language codes matching our 14 locales
    private val SUPPORTED_LANGUAGES = setOf(
        "en", "ar", "da", "de", "fr", "in", "ja", "ko", "ms", "nb", "nl", "sv", "th", "tr"
    )

    /**
     * Get the currently selected language code.
     * Returns the stored preference, or detects from device locale if not set.
     */
    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val stored = prefs.getString(KEY_LANGUAGE, null)

        if (stored != null) {
            return stored
        }

        // First launch - detect device locale
        val deviceLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }

        val deviceLang = deviceLocale.language
        return if (SUPPORTED_LANGUAGES.contains(deviceLang)) deviceLang else "en"
    }

    /**
     * Set the app language and persist to SharedPreferences.
     * Uses commit() instead of apply() to ensure synchronous write before activity recreation.
     */
    fun setLanguage(context: Context, languageCode: String) {
        require(SUPPORTED_LANGUAGES.contains(languageCode)) {
            "Unsupported language: $languageCode"
        }

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, languageCode)
            .commit()  // Synchronous write - critical before activity.recreate()
    }

    /**
     * Check if language has been explicitly set by user.
     * Returns false on first launch (before language selection).
     */
    fun isLanguageSet(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(KEY_LANGUAGE)
    }

    /**
     * Apply the stored locale to the given context.
     * This wraps the context with a new configuration.
     * Should be called from attachBaseContext() in Activity and Application.
     */
    fun applyLocale(baseContext: Context): Context {
        val languageCode = getLanguage(baseContext)
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration(baseContext.resources.configuration)
        configuration.setLocale(locale)

        return baseContext.createConfigurationContext(configuration)
    }
}
