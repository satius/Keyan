package net.satius.keyan.infrastructure.sesame.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.satius.keyan.core.domain.sesame.account.SesameAccount
import net.satius.keyan.core.domain.sesame.account.SesameDeviceAccountMixin
import net.satius.keyan.core.domain.sesame.api.SesameWebApiRepository
import net.satius.keyan.core.domain.sesame.api.SesameWebApiRepository.*
import net.satius.keyan.core.domain.sesame.api.SesameWebApiRepository.SesameControlType.*
import net.satius.keyan.core.domain.sesame.device.SesameDeviceImpl
import net.satius.keyan.infrastructure.sesame.api.SesameRetrofitService.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import retrofit2.Call as RCall
import retrofit2.Callback as RCallback
import retrofit2.Response as RResponse

// TODO: モックも作成
class SesameWebApiRepositoryImpl : SesameWebApiRepository, CoroutineScope {
    companion object {
        private const val END_POINT = "https://api.candyhouse.co/public/"
    }

    override val coroutineContext = Dispatchers.Default

    private val retrofit by lazy {
        // TODO: Productionではログ出さない
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .build()

        Retrofit
            .Builder()
            .baseUrl(END_POINT)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val service by lazy { retrofit.create(SesameRetrofitService::class.java) }

    override suspend fun getSesameDevices(account: SesameAccount): GetSesameDevicesResult =
        suspendCoroutine { continuation ->

            val callback = object : RCallback<List<GetSesameDeviceItem>> {
                override fun onFailure(call: RCall<List<GetSesameDeviceItem>>, t: Throwable) {
                    continuation.resume(GetSesameDevicesResult.Error(t))
                }

                override fun onResponse(
                    call: RCall<List<GetSesameDeviceItem>>,
                    response: RResponse<List<GetSesameDeviceItem>>
                ) {
                    response.takeIf { it.isSuccessful }?.body()?.let { body ->
                        val list = body.map {
                            SesameDeviceImpl(
                                serial = it.serial,
                                accountId = account.accountId,
                                deviceId = it.deviceId,
                                nickName = it.nickname
                            )
                        }
                        continuation.resume(GetSesameDevicesResult.Success(list))
                    } ?: run {
                        val error = when (response.code()) {
                            // TODO
                            else -> null
                        }
                        continuation.resume(GetSesameDevicesResult.Error(error))
                    }
                }
            }
            service.getSesameDevices(account.authToken).enqueue(callback)
        }

    override suspend fun getSesameStatus(mixin: SesameDeviceAccountMixin): SesameStatus {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun controlSesameDevice(
        mixin: SesameDeviceAccountMixin,
        control: SesameControlType
    ): ControlResult = suspendCoroutine { continuation ->

        val callback = object : RCallback<ControlSesameDeviceResponseBody> {
            override fun onFailure(
                call: RCall<ControlSesameDeviceResponseBody>,
                t: Throwable
            ) {
                continuation.resume(ControlResult.Error(t))
            }

            override fun onResponse(
                call: RCall<ControlSesameDeviceResponseBody>,
                response: RResponse<ControlSesameDeviceResponseBody>
            ) {
                response.takeIf { it.isSuccessful }?.body()?.let { body ->
                    continuation.resume(ControlResult.Executed(body.taskId))

                } ?: run {
                    val error = when (response.code()) {
                        // TODO
                        else -> null
                    }
                    continuation.resume(ControlResult.Error(error))
                }
            }
        }

        val controlCommand = when (control) {
            LOCK -> "lock"
            UNLOCK -> "unlock"
            SYNC -> "sync"
        }

        service
            .controlSesameDevice(
                authToken = mixin.authToken,
                deviceId = mixin.deviceId,
                body = ControlSesameDeviceRequestBody(
                    command = controlCommand
                )
            )
            .enqueue(callback)
    }

    override suspend fun getExecutionResult(
        account: SesameAccount,
        taskId: String
    ): ExecutionResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
