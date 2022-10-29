package com.teamtwo.carparkfinderapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CarparkApplication: Application() {
    // set up logging
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}