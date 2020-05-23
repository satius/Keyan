package net.satius.keyan.infrastructure.sesame.device

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SesameDeviceAndroidBootLaunchEntity(
    @PrimaryKey
    val serial: String
)
