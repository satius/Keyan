package net.satius.keyan.core.usecase.notification

import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin
import net.satius.keyan.core.domain.sesame.api.SesameWebApiRepository
import org.koin.core.KoinComponent
import org.koin.core.inject


class NotificationUseCaseImpl : NotificationUseCase, KoinComponent {

    private val sesameWebApiRepository by inject<SesameWebApiRepository>()

    override suspend fun unlock(mixin: SesameDeviceAccountMixin) {
        // TODO: 前後の状態もチェック
        sesameWebApiRepository.controlSesameDevice(
            mixin,
            SesameWebApiRepository.SesameControlType.UNLOCK
        )
    }

    override suspend fun lock(mixin: SesameDeviceAccountMixin) {
        // TODO: 前後の状態もチェック
        sesameWebApiRepository.controlSesameDevice(
            mixin,
            SesameWebApiRepository.SesameControlType.LOCK
        )

    }

    override suspend fun refresh(mixin: SesameDeviceAccountMixin) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
