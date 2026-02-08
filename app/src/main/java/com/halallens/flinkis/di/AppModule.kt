package com.halallens.flinkis.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// DataStore extension for preferences
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "myroutine_preferences"
)

/**
 * App Module - Provides application-wide dependencies
 *
 * Uses Kotlin 2.1.0+ compatible pattern:
 * - abstract class (not object)
 * - companion object with @JvmStatic @Provides
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context = context

        @JvmStatic
        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }
    }
}
