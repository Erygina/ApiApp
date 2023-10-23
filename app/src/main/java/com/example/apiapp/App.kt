package com.example.apiapp

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        Log.i(TAG, "App.onCreate: STARTED..")
        startKoin()
    }

    private fun startKoin() {
        // Start Koin
        Log.i(TAG, "App.startKoin: Start Koin..")
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule,
                ),
            )
        }
        Log.i(TAG, "App.startKoin: Done.")
    }

}