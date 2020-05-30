package net.satius.keyan.core.domain.sesame.api

import net.satius.keyan.core.domain.sesame.account.SesameAccount
import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin
import net.satius.keyan.core.domain.sesame.device.SesameDevice

interface SesameWebApiRepository {

    suspend fun getSesameDevices(account: SesameAccount): GetSesameDevicesResult
    suspend fun getSesameStatus(mixin: SesameDeviceAccountMixin): SesameStatus
    suspend fun controlSesameDevice(
        mixin: SesameDeviceAccountMixin,
        control: SesameControlType
    ): ControlResult

    suspend fun getExecutionResult(account: SesameAccount, taskId: String): ExecutionResult

    data class SesameStatus(
        val isLocked: Boolean,
        val battery: Int,
        val isResponsive: Boolean
    )

    enum class SesameControlType {
        LOCK, UNLOCK, SYNC
    }

    sealed class ControlResult {
        data class Executed(val taskId: String) : ControlResult()
        data class Error(val e: Throwable?) : ControlResult()
    }

    data class ExecutionResult(
        val taskStatus: TaskStatus,
        val isSuccessful: Boolean,
        val error: String
    ) {
        enum class TaskStatus {
            PROCESSING, TERMINATED
        }
    }

    sealed class GetSesameDevicesResult {
        data class Success(val list: List<SesameDevice>) : GetSesameDevicesResult()
        data class Error(val e: Throwable?) : GetSesameDevicesResult()
    }
}