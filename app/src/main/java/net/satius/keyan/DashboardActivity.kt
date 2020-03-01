package net.satius.keyan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import net.satius.keyan.notification.keyoperation.KeyOperatableNotification.Companion.CHANNEL_ID
import net.satius.keyan.notification.keyoperation.KeyOperatableNotificationImpl

class DashboardActivity : AppCompatActivity() {

    private val buttonStartService by lazy { findViewById<Button>(R.id.button_start_service) }
    private val buttonStopService by lazy { findViewById<Button>(R.id.button_stop_service) }

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val keyOperatableNotification by lazy { KeyOperatableNotificationImpl(this) } // TODO: DI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ("Dashboard")
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

    private fun startService() {
        keyOperatableNotification.startForegroundService()
    }

    private fun stopService() {
        keyOperatableNotification.stopForegroundService()
    }

}
