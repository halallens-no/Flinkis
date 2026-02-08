package com.halallens.flinkis.domain.model

/**
 * Theme types available for the app.
 * Selected during onboarding, persisted in DataStore.
 */
enum class ThemeType(val displayName: String) {
    BOY("Adventure"),
    GIRL("Sparkle"),
    NEUTRAL("Nature")
}
