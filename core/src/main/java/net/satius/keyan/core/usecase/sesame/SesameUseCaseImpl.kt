package net.satius.keyan.core.usecase.sesame

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.satius.keyan.core.domain.sesame.account.SesameAccount
import net.satius.keyan.core.domain.sesame.account.SesameAccountRepository
import net.satius.keyan.core.domain.sesame.account.SesameAccountWithDevices
import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin
import net.satius.keyan.core.domain.sesame.api.SesameWebApiRepository
import net.satius.keyan.core.domain.sesame.device.SesameDevice
import net.satius.keyan.core.domain.sesame.device.SesameDeviceRepository
import net.satius.keyan.core.usecase.sesame.SesameUseCase.FetchSesameDevicesResult
import org.koin.core.KoinComponent
import org.koin.core.inject

class SesameUseCaseImpl : SesameUseCase, KoinComponent, CoroutineScope {

    override val coroutineContext = Dispatchers.Default

    private val sesameDeviceRepository: SesameDeviceRepository by inject()
    private val sesameAccountRepository: SesameAccountRepository by inject()
    private val sesameWebApiRepository: SesameWebApiRepository by inject()

    override suspend fun createAccount(name: String, authToken: String): SesameAccount {
        // TODO アカウントがキャンディハウスに存在するか確認
        return sesameAccountRepository.upsert(name, authToken)
    }

    override suspend fun getAllLocalAccounts(): List<SesameAccount> {
        // TODO アカウントがキャンディハウスに存在するか確認
        return sesameAccountRepository.getAll()
    }

    override suspend fun fetchSesameDevices(account: SesameAccount): FetchSesameDevicesResult {
        return when (val result = sesameWebApiRepository.getSesameDevices(account)) {
            is SesameWebApiRepository.GetSesameDevicesResult.Success -> {
                FetchSesameDevicesResult.Success(
                    SesameAccountWithDevices(account, result.list)
                )
            }

            is SesameWebApiRepository.GetSesameDevicesResult.Error
            -> FetchSesameDevicesResult.Error(result.e)
        }
    }

    override suspend fun registerBootLaunchDevice(device: SesameDevice) {
        sesameDeviceRepository.upsertBootLaunchDeviceSerials(device)
    }

    override suspend fun getBootLaunchDevices(): List<SesameDeviceAccountMixin> {
        val accounts = sesameAccountRepository.getAll()
        val deviceList =
            accounts
                .map {
                    when (val result = sesameWebApiRepository.getSesameDevices(it)) {
                        is SesameWebApiRepository.GetSesameDevicesResult.Success -> {
                            SesameAccountWithDevices(it, result.list).devices
                        }
                        // TODO: エラーハンドリング検討
                        is SesameWebApiRepository.GetSesameDevicesResult.Error -> {
                            return listOf()
                        }
                    }
                }
                .flatten()

        val bootLaunchSerialList = sesameDeviceRepository.getBootLaunchDeviceSerials()

        return deviceList.filter { bootLaunchSerialList.contains(it.serial) }
    }

}
