package net.satius.keyan

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    private val buttonStartService by lazy { findViewById<Button>(R.id.button_start_service) }
    private val buttonStopService by lazy { findViewById<Button>(R.id.button_stop_service) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ("Dashboard")

        setupViews()
    }

    private fun setupViews() {
        buttonStartService.setOnClickListener { startService() }
        buttonStopService.setOnClickListener { stopService() }
    }

    private fun startService() {
        TODO()
    }

    private fun stopService() {
        TODO()
    }

}
