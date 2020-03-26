package net.satius.keyan.core.domain.notification

import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin

interface NotificationRepository {
    fun start(mixin: SesameDeviceAccountMixin)
    fun stop(mixin: SesameDeviceAccountMixin)
}
