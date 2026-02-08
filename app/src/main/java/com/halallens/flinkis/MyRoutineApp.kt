package com.halallens.flinkis

import android.app.Application
import android.content.Context
import com.halallens.flinkis.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

/**
 * MyRoutine Application class
 *
 * @HiltAndroidApp triggers Hilt's code generation
 * This is the entry point for dependency injection
 */
@HiltAndroidApp
class MyRoutineApp : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(base))
    }

    override fun onCreate() {
        super.onCreate()
    }
}
