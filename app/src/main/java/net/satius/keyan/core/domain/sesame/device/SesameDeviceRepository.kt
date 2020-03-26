package net.satius.keyan.core.domain.sesame.device

interface SesameDeviceRepository {
    suspend fun upsertBootLaunchDeviceSerials(device: SesameDevice)
    suspend fun deleteBootLaunchDeviceSerials(device: SesameDevice)
    suspend fun getBootLaunchDeviceSerials(): List<String>
}
