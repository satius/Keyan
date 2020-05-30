package net.satius.keyan

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import net.satius.keyan.controller.receiver.AndroidActionReceiver
import net.satius.keyan.registry.createDataBaseModules
import net.satius.keyan.registry.createRepositoryModules
import net.satius.keyan.registry.createUseCaseModules
import net.satius.keyan.registry.createUtilModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KeyanApplication : Application() {

    private val androidActionReceiver by lazy { AndroidActionReceiver() }

    override fun onCreate() {
        super.onCreate()
        di()
        setupAndroidActionReceiver()
    }

    private fun di() {
        startKoin {
            androidContext(applicationContext)
            modules(createRepositoryModules())
            modules(createUtilModules(applicationContext))
            modules(createDataBaseModules(applicationContext))
            modules(createUseCaseModules())
        }
    }

    private fun setupAndroidActionReceiver() {
        registerReceiver(
            androidActionReceiver,
            IntentFilter().apply { addAction(Intent.ACTION_USER_PRESENT) }
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(androidActionReceiver)
    }
}
