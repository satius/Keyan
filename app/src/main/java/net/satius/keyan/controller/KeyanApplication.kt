package net.satius.keyan.controller

import android.app.Application
import net.satius.keyan.core.di.createDataBaseModules
import net.satius.keyan.core.di.createRepositoryModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KeyanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        di()
    }

    private fun di() {
        startKoin {
            androidContext(applicationContext)
            modules(createRepositoryModules(applicationContext))
            modules(createDataBaseModules(applicationContext))
        }
    }

}
