package net.satius.keyan.core.usecase.notification

import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin

interface NotificationUseCase {
    suspend fun unlock(mixin: SesameDeviceAccountMixin)
    suspend fun lock(mixin: SesameDeviceAccountMixin)
    suspend fun refresh(mixin: SesameDeviceAccountMixin)
}
