package net.satius.keyan.controller.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.satius.keyan.controller.service.NotificationService
import net.satius.keyan.core.usecase.sesame.SesameUseCase
import org.koin.core.KoinComponent
import org.koin.core.inject

class AndroidActionReceiver : BroadcastReceiver(), KoinComponent, CoroutineScope {

    private val sesameUseCase by inject<SesameUseCase>()
    override val coroutineContext = Dispatchers.Default

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_USER_PRESENT -> {

                launch {
                    val testMixin = async {
                        sesameUseCase.getBootLaunchDevices().firstOrNull()
                    }

                    testMixin.await()?.let {
                        if (!NotificationService.isAlreadyStarted(it)) {
                            NotificationService.startService(context, it)
                        } else {
                            // TODO : Error handling
                            println()
                        }
                    }
                }
            }
        }
    }
}
