package net.satius.keyan.infrastructure.sesame.device

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.satius.keyan.core.domain.sesame.device.SesameDevice
import net.satius.keyan.core.domain.sesame.device.SesameDeviceRepository
import net.satius.keyan.infrastructure.common.KeyanDataBase
import org.koin.core.KoinComponent
import org.koin.core.inject

class SesameDeviceRepositoryImpl : SesameDeviceRepository, KoinComponent {

    private val db by inject<KeyanDataBase>()
    private val launchDao by lazy { db.sesameDeviceAndroidBootLaunchDao() }

    override suspend fun upsertBootLaunchDeviceSerials(device: SesameDevice) {
        val targetSerial = device.serial

        val allSerials = withContext(Dispatchers.IO) {
            launchDao.findAll().map { it.serial }
        }

        if (allSerials.contains(targetSerial)) return

        withContext(Dispatchers.IO) {
            launchDao.create(
                SesameDeviceAndroidBootLaunchEntity(
                    targetSerial
                )
            )
        }
    }

    override suspend fun deleteBootLaunchDeviceSerials(device: SesameDevice) {
        // TODO: 消えないかも。テスト必要
        withContext(Dispatchers.IO) {
            launchDao.delete(
                SesameDeviceAndroidBootLaunchEntity(
                    device.serial
                )
            )
        }
    }

    override suspend fun getBootLaunchDeviceSerials(): List<String> {
        return withContext(Dispatchers.IO) {
            launchDao.findAll().map { it.serial }
        }
    }

}
