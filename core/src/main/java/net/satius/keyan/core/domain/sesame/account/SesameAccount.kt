package net.satius.keyan.core.domain.sesame.account

import net.satius.keyan.core.domain.sesame.device.SesameDevice
import java.io.Serializable

interface SesameAccount : Serializable {
    var name: String
    val authToken: String
    val accountId: Long
}

data class SesameAccountImpl(
    override var name: String,
    override val authToken: String,
    override val accountId: Long
) : SesameAccount

data class SesameAccountWithDevices(
    override var name: String,
    override val authToken: String,
    override val accountId: Long,
    val devices: List<SesameDeviceAccountMixin>
) : SesameAccount {
    constructor(account: SesameAccount, devices: List<SesameDevice>) : this(
        name = account.name,
        authToken = account.authToken,
        accountId = account.accountId,
        devices = devices.map {
            SesameDeviceAccountMixin.from(it, account)
        }
    )

    init {
        check(devices.all {
            it.accountId == accountId
                    && it.authToken == authToken
                    && it.name == name
        })
    }
}

data class SesameDeviceAccountMixin(
    override var name: String,
    override val authToken: String,
    override val serial: String,
    override val accountId: Long,
    override val deviceId: String,
    override val nickName: String
) : SesameAccount, SesameDevice {

    companion object {
        fun from(device: SesameDevice, account: SesameAccount): SesameDeviceAccountMixin {
            check(device.accountId == account.accountId)
            return SesameDeviceAccountMixin(
                accountId = device.accountId,
                serial = device.serial,
                deviceId = device.deviceId,
                nickName = device.nickName,
                name = account.name,
                authToken = account.authToken
            )
        }
    }
}
