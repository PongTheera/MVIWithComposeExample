package com.example.mviwithcomposeexample

import android.app.Application
import com.example.mviwithcomposeexample.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class MVIApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MVIApplication)
            modules(mutableListOf<Module>().apply {
                addAll(appModules)
            })
        }
    }

}