package net.satius.keyan.controller.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.satius.keyan.controller.service.NotificationService
import net.satius.keyan.controller.service.NotificationService.ActionType.*
import net.satius.keyan.controller.view.DashboardActivity
import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin
import net.satius.keyan.core.usecase.notification.NotificationUseCase
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotificationActionReceiver : BroadcastReceiver(), KoinComponent, CoroutineScope {

    override val coroutineContext = Dispatchers.Default

    private val notificationUseCase by inject<NotificationUseCase>()

    companion object {

        private const val ACTION_TYPE = "ACTION_TYPE"
        private const val SESAME_MIXIN = "SESAME_MIXIN"

        fun createIntent(
            fromContext: Context,
            sesameDeviceAccountMixin: SesameDeviceAccountMixin,
            actionType: NotificationService.ActionType

        ) = Intent(fromContext, NotificationActionReceiver::class.java).apply {
            putExtra(SESAME_MIXIN, sesameDeviceAccountMixin)
            putExtra(ACTION_TYPE, actionType)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {

        val actionType =
            intent.getSerializableExtra(ACTION_TYPE) as? NotificationService.ActionType ?: return

        val mixin =
            intent.getSerializableExtra(SESAME_MIXIN) as? SesameDeviceAccountMixin ?: return

        launch {
            // TODO: それぞれ結果を通知に反映
            when (actionType) {
                LOCK -> notificationUseCase.lock(mixin)
                UNLOCK -> notificationUseCase.unlock(mixin)
                REFRESH -> notificationUseCase.refresh(mixin)
                LAUNCH -> {
                    val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                    context.sendBroadcast(closeIntent) // Close notifications drawer
                    DashboardActivity.start(context)
                }
            }.let {}
        }
    }
}
