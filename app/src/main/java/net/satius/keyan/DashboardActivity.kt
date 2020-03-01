package net.satius.keyan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import net.satius.keyan.notification.keyoperation.KeyOperatableNotificationControllerImpl
import net.satius.keyan.notification.keyoperation.KeyOperatableNotificationService.Companion.CHANNEL_ID

class DashboardActivity : AppCompatActivity() {

    private val buttonStartService by lazy { findViewById<Button>(R.id.button_start_service) }
    private val buttonStopService by lazy { findViewById<Button>(R.id.button_stop_service) }

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val KeyOperatableNotificationController by lazy {
        KeyOperatableNotificationControllerImpl(
            this
        )
    } // TODO: DI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ("Dashboard")

        // TODO: Channelは重複して作成しないようにチェックを実装
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Sample channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        setupViews()
    }

    private fun setupViews() {
        buttonStartService.setOnClickListener { startService() }
        buttonStopService.setOnClickListener { stopService() }
    }

    // TODO: 端末起動時にも通知送信できるようにローカルDB上の設定項目書き換え
    private fun startService() {
        KeyOperatableNotificationController.startForegroundService()
    }

    private fun stopService() {
        KeyOperatableNotificationController.stopForegroundService()
    }

}
