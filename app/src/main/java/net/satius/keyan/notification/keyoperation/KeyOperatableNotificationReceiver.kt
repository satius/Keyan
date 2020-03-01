package net.satius.keyan.notification.keyoperation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class KeyOperatableNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println(intent.action)
        // TODO
    }
}
