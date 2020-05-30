package net.satius.keyan.controller.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import net.satius.keyan.R
import net.satius.keyan.controller.receiver.NotificationActionReceiver
import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin

class NotificationService : Service() {

    companion object {
        /**
         * 同じチャンネルのService重複起動防止の監視用
         * TODO: GC対象で挙動が狂う可能性を検証
         */
        private val servingList = mutableListOf<NotificationService>()

        private const val FOREGROUND_SERVICE_ID = 1000
        private const val CHANNEL_ID_PREFIX = "KeyanNotification:"
        private const val CHANNEL_NAME = "Keyan Controller"

        private const val SESAME_MIXIN = "SESAME_MIXIN"

        private fun createIntent(
            fromContext: Context,
            // TODO: device状態 は必要なさそう？
            sesameDeviceAccountMixin: SesameDeviceAccountMixin

        ) = Intent(fromContext, NotificationService::class.java).apply {
            putExtra(SESAME_MIXIN, sesameDeviceAccountMixin)
        }

        fun isAlreadyStarted(sesameDeviceAccountMixin: SesameDeviceAccountMixin): Boolean {
            return servingList.any { it.getChannelId() == createChannelId(sesameDeviceAccountMixin) }
        }

        fun startService(
            fromContext: Context,
            sesameDeviceAccountMixin: SesameDeviceAccountMixin
        ) {
            val intent = createIntent(fromContext, sesameDeviceAccountMixin)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                fromContext.startForegroundService(intent)
            } else {
                fromContext.startService(intent)
            }
        }

        fun stopService(
            fromContext: Context,
            sesameDeviceAccountMixin: SesameDeviceAccountMixin
        ) {
            val notificationManager = fromContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = createChannelId(sesameDeviceAccountMixin)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.deleteNotificationChannel(channelId)
            }

            val intent = createIntent(fromContext, sesameDeviceAccountMixin)

            servingList.removeAll(servingList.filter {
                it.getChannelId() == createChannelId(
                    sesameDeviceAccountMixin
                )
            })
            fromContext.stopService(intent)
        }

        fun createChannelId(mixin: SesameDeviceAccountMixin) = CHANNEL_ID_PREFIX + mixin.serial
    }

    private lateinit var mixin: SesameDeviceAccountMixin

    private fun getChannelId() = createChannelId(mixin)

    enum class ActionType(
        @DrawableRes val iconDrawable: Int,
        val title: String,
        val showInCompactView: Boolean
    ) {
        LOCK(
            R.drawable.baseline_lock_24,
            "Lock",
            true
        ),
        UNLOCK(
            R.drawable.baseline_lock_open_24,
            "Unlock",
            true
        ),
        REFRESH(
            R.drawable.baseline_refresh_24,
            "Refresh",
            false
        ),
        LAUNCH(
            R.drawable.baseline_settings_24,
            "Launch",
            false
        )
    }

    override fun onBind(p0: Intent?): IBinder? = null

//    override fun onCreate() {
//        super.onCreate()
//        /* NOP */
//    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        mixin = intent.getSerializableExtra(SESAME_MIXIN) as SesameDeviceAccountMixin

        val channelId = createChannelId(mixin)

        createNotificationChannelIfNeeded(
            context = applicationContext,
            channelId = channelId
        )
        servingList.add(this)
        startForeground(FOREGROUND_SERVICE_ID, createNotification(channelId))
        return START_STICKY
    }

    private fun createNotification(channelId: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.key)
            .setContentTitle(mixin.nickName)
            .setContentText(mixin.serial)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setColorized(true)
            .setMediaStyleAndAddActions(
                this,
                ActionType.values().toList()
            )
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.sesames))
            .build()
    }

    private fun NotificationCompat.Builder.setMediaStyleAndAddActions(
        context: Context,
        typeList: List<ActionType>
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

        typeList.forEach { type ->
            addAction(
                type.iconDrawable,
                type.title,
                PendingIntent.getBroadcast(
                    context,
                    type.ordinal,
                    NotificationActionReceiver.createIntent(
                        context,
                        mixin,
                        type
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }
        return this
    }

    private fun createNotificationChannelIfNeeded(context: Context, channelId: String) {
        val notificationManager by lazy {
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            !notificationManager.notificationChannels.any { it.id == channelId }
        ) {
            val channel = NotificationChannel(
                channelId,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}
