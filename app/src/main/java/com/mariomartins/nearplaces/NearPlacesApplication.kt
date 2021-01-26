package com.mariomartins.nearplaces

import android.app.Application
import com.mariomartins.nearplaces.data.dataModule
import com.mariomartins.nearplaces.domain.domainModule
import com.mariomartins.nearplaces.features.featuresModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class NearPlacesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin()

        if (BuildConfig.DEBUG) startLogging()
    }

    private fun startKoin() = startKoin {
        androidLogger(Level.DEBUG)
        androidContext(this@NearPlacesApplication)
        modules(dataModule + domainModule + featuresModule)
    }

    private fun startLogging() = Timber.plant(Timber.DebugTree())
}
