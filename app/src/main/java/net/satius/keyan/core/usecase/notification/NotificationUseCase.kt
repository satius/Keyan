package net.satius.keyan.core.usecase.notification

import android.content.Context
import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin

interface NotificationUseCase {
    fun start(mixin: SesameDeviceAccountMixin)
    fun stop(mixin: SesameDeviceAccountMixin)
    suspend fun unlock(mixin: SesameDeviceAccountMixin)
    suspend fun lock(mixin: SesameDeviceAccountMixin)
    suspend fun refresh(mixin: SesameDeviceAccountMixin)
    fun launch(context: Context)
}
