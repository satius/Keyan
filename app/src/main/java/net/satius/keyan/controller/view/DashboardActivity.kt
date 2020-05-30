package net.satius.keyan.controller.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.coroutines.launch
import net.satius.keyan.R
import net.satius.keyan.controller.service.NotificationService
import net.satius.keyan.core.domain.sesame.account.SesameAccount
import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin
import net.satius.keyan.core.usecase.notification.NotificationUseCase
import net.satius.keyan.core.usecase.sesame.SesameUseCase
import org.koin.android.ext.android.inject

class DashboardActivity : ScopedActivity() {

    private val sesameUseCase: SesameUseCase by inject()
    private val notificationUseCase: NotificationUseCase by inject()

    private val buttonStartService by lazy { findViewById<Button>(R.id.button_start_service) }
    private val buttonStopService by lazy { findViewById<Button>(R.id.button_stop_service) }

    companion object {
        fun start(fromContext: Context) = fromContext.startActivity(
            createIntent(fromContext).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )

        fun start(fromActivity: Activity) = fromActivity.startActivity(createIntent(fromActivity))

        private fun createIntent(fromContext: Context) =
            Intent(fromContext, DashboardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = ("Dashboard")

        buttonStartService.visibility = View.INVISIBLE
        buttonStopService.visibility = View.INVISIBLE

        launch {
            val account = testAccount() ?: return@launch

            when (val accountDevices = getDevices(account)) {
                is SesameUseCase.FetchSesameDevicesResult.Success -> {
                    accountDevices.value.devices.firstOrNull()?.let {
                        sesameUseCase.registerBootLaunchDevice(it)
                        setupViews(it)
                    }
                }
                is SesameUseCase.FetchSesameDevicesResult.Error -> {
                    println(accountDevices.t)
                }
            }.let {}
        }
    }

    private fun setupViews(mixin: SesameDeviceAccountMixin) {
        buttonStartService.setOnClickListener { startService(mixin) }
        buttonStopService.setOnClickListener { stopService(mixin) }
        buttonStartService.visibility = View.VISIBLE
        buttonStopService.visibility = View.VISIBLE
    }

    private suspend fun testAccount() = sesameUseCase.getAllLocalAccounts().firstOrNull()

    private suspend fun getDevices(account: SesameAccount) =
        sesameUseCase.fetchSesameDevices(account)

    private fun startService(mixin: SesameDeviceAccountMixin) {
        if (!NotificationService.isAlreadyStarted(mixin)) {
            NotificationService.startService(this, mixin)
        } else {
            // TODO : Error handling
            println()
        }
    }

    private fun stopService(mixin: SesameDeviceAccountMixin) {
        if (NotificationService.isAlreadyStarted(mixin)) {
            NotificationService.stopService(this, mixin)
        } else {
            // TODO : Error handling
            println()
        }
    }

}
