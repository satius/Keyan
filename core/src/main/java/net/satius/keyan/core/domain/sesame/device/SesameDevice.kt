package net.satius.keyan.core.domain.sesame.device

import java.io.Serializable

interface SesameDevice : Serializable {
    val serial: String
    val accountId: Long
    val deviceId: String
    val nickName: String
}

data class SesameDeviceImpl(
    override val serial: String,
    override val accountId: Long,
    override val deviceId: String,
    override val nickName: String
) : SesameDevice
