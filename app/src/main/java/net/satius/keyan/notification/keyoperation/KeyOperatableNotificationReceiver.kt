package net.satius.keyan.notification.keyoperation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class KeyOperatableNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println(intent.action)

        // TODO: 通知送信のタイミングは、端末起動時か、ロック画面解除時でユーザ設定可能にする
        // TODO: 通知操作はControllerに委譲
        when (intent.action) {
            // 端末起動時に送信されるインテント
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                Intent(
                    context.applicationContext,
                    KeyOperatableNotificationService::class.java
                ).also {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(it)
                    } else {
                        context.startService(it)

                    }
                }
            }

            // ロック解除時のインテント
            Intent.ACTION_USER_PRESENT -> {
                // TODO

            }

            else -> {
                // TODO: セサミ施錠/解錠など
            }
        }

    }
}
