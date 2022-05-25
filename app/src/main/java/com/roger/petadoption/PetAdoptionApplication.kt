package com.roger.petadoption

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PetAdoptionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            /*
            * Stetho usage:
            * 1. Launch app.
            * 2. Open chrome browser and key in chrome://inspect.
            * */
            Stetho.initializeWithDefaults(this)
        }
    }
}