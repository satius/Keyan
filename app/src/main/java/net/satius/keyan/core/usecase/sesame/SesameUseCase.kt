package net.satius.keyan.core.usecase.sesame

import net.satius.keyan.core.domain.sesame.account.SesameAccount
import net.satius.keyan.core.domain.sesame.account.SesameAccountWithDevices
import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin
import net.satius.keyan.core.domain.sesame.device.SesameDevice

interface SesameUseCase {
    suspend fun createAccount(name: String, authToken: String): SesameAccount
    suspend fun getAllLocalAccounts(): List<SesameAccount>

    suspend fun fetchSesameDevices(account: SesameAccount): FetchSesameDevicesResult
    suspend fun registerBootLaunchDevice(device: SesameDevice)
    suspend fun getBootLaunchDevices(): List<SesameDeviceAccountMixin>

    sealed class FetchSesameDevicesResult {
        data class Success(val value: SesameAccountWithDevices) : FetchSesameDevicesResult()
        data class Error(val t: Throwable?) : FetchSesameDevicesResult()
    }
}
