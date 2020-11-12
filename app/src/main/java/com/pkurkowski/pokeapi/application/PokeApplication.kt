package com.pkurkowski.pokeapi.application

import android.app.Application
import com.pkurkowski.pokeapi.BuildConfig
import com.pkurkowski.pokeapi.application.di.applicationModule
import com.pkurkowski.pokeapi.application.di.remoteDataSourceModule
import com.pkurkowski.pokeapi.application.di.roomDataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class PokeApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@PokeApplication)
            // module list
            modules(applicationModule, remoteDataSourceModule, roomDataSourceModule)
        }
    }

}