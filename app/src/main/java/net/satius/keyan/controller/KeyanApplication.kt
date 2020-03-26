package net.satius.keyan.controller

import android.app.Application

class KeyanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        di()
    }

    private fun di() {
    }

}
