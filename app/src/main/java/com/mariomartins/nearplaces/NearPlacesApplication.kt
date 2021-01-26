package com.mariomartins.nearplaces

import android.app.Application
import com.mariomartins.nearplaces.data.dataModule
import com.mariomartins.nearplaces.domain.domainModule
import com.mariomartins.nearplaces.features.featuresModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

class NearPlacesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(getString(R.string.google_maps_key))

        if (BuildConfig.DEBUG) startLogging()
    }

    private fun startKoin(apiKey: String) = startKoin {
        val keyModule = module { single(named(GOOGLE_API_KEY_SCOPE)) { apiKey } }

        androidLogger(Level.DEBUG)
        androidContext(this@NearPlacesApplication)
        modules(dataModule + domainModule + featuresModule + keyModule)
    }

    private fun startLogging() = Timber.plant(Timber.DebugTree())

    companion object {

        const val GOOGLE_API_KEY_SCOPE = "GOOGLE_API_KEY_SCOPE"
    }
}
