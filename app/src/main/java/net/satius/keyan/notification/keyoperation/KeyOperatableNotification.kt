package net.satius.keyan.notification.keyoperation

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import net.satius.keyan.R
import net.satius.keyan.notification.keyoperation.KeyOperatableNotification.Companion.CHANNEL_ID

interface KeyOperatableNotification {
    companion object {
        const val CHANNEL_ID = "KeyanNotification"
    }

    enum class ActionType(
        val prop: String,
        @DrawableRes val iconDrawable: Int,
        val title: String,
        val showInCompactView: Boolean
    ) {
        LOCK(
            "lock456787678",
            R.drawable.baseline_lock_24,
            "Lock",
            true
        ),
        UNLOCK(
            "unlock876567897890",
            R.drawable.baseline_lock_open_24,
            "Unlock",
            true
        ),
        REFRESH(
            "refresh0987890-",
            R.drawable.baseline_refresh_24,
            "Refresh",
            false
        ),
        LAUNCH(
            "launch567890-0",
            R.drawable.baseline_settings_24,
            "Launch",
            false
        )
    }

    fun startForegroundService()

    fun stopForegroundService()

}

class KeyOperatableNotificationImpl(private val context: Context) : KeyOperatableNotification {

    private var serviceIntent = Intent(context, NotificationService::class.java)

    override fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    override fun stopForegroundService() {
        context.stopService(serviceIntent)
    }

    class NotificationService : Service() {

        companion object {
            const val ID = 1000
        }

        override fun onBind(p0: Intent?): IBinder? = null

        override fun onCreate() {
            super.onCreate()
            startForeground(ID, createNotification())
        }

        private fun createNotification(): Notification {
            return NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.sesames) // TODO: fix
                .setContentTitle("イエノカギ") // TODO
                .setContentText("Locked/Unlocked") // TODO
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // TODO: ユーザ設定
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setColorized(true)
                .setMediaStyleAndAddActions(
                    this,
                    KeyOperatableNotification.ActionType.values().toList()
                )
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.sesames))
                .build()
        }

        private fun NotificationCompat.Builder.addAction(
            context: Context,
            type: KeyOperatableNotification.ActionType
        ) = addAction(
            type.iconDrawable,
            type.title,
            PendingIntent.getBroadcast(
                context,
                type.ordinal,
                Intent(context, KeyOperatableNotificationReceiver::class.java).apply {
                    action = type.prop
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        private fun NotificationCompat.Builder.setMediaStyleAndAddActions(
            context: Context,
            typeList: List<KeyOperatableNotification.ActionType>
        ): NotificationCompat.Builder {

            val actionArray = mutableListOf<Int>().apply {
                typeList.forEachIndexed { idx, it ->
                    if (it.showInCompactView) {
                        add(idx)
                    }
                }
            }.toIntArray()

            setStyle(
                androidx.media.app.NotificationCompat
                    .MediaStyle()
                    .setShowActionsInCompactView(*actionArray)
            )

            typeList.forEach { addAction(context, it) }
            return this
        }
    }
}
