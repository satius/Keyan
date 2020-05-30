package net.satius.keyan.infrastructure.sesame.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Call
import retrofit2.http.*

// TODO: OpenAPI-generator-Kotlinで生成する
interface SesameRetrofitService {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
    }

    @GET("sesames")
    fun getSesameDevices(
        @Header(AUTHORIZATION_HEADER)
        authToken: String
    ): Call<List<GetSesameDeviceItem>>


    @POST("sesame/{deviceId}")
    fun controlSesameDevice(
        @Header(AUTHORIZATION_HEADER)
        authToken: String,
        @Path("deviceId")
        deviceId: String,
        @Body
        body: ControlSesameDeviceRequestBody
    ): Call<ControlSesameDeviceResponseBody>


    @JsonClass(generateAdapter = true)
    data class GetSesameDeviceItem(
        @Json(name = "device_id")
        val deviceId: String,
        @Json(name = "serial")
        val serial: String,
        @Json(name = "nickname")
        val nickname: String
    )

    @JsonClass(generateAdapter = true)
    data class ControlSesameDeviceRequestBody(
        @Json(name = "command")
        val command: String
    )

    @JsonClass(generateAdapter = true)
    data class ControlSesameDeviceResponseBody(
        @Json(name = "task_id")
        val taskId: String
    )
}
