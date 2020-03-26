package net.satius.keyan.infrastructure.common

import android.util.Base64
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import net.satius.keyan.infrastructure.sesame.account.SesameAccountDao
import net.satius.keyan.infrastructure.sesame.account.SesameAccountEntity
import net.satius.keyan.infrastructure.sesame.device.SesameDeviceAndroidBootLaunchDao
import net.satius.keyan.infrastructure.sesame.device.SesameDeviceAndroidBootLaunchEntity
import net.satius.keyan.infrastructure.user.preferences.DisplayTheme
import net.satius.keyan.infrastructure.user.preferences.UserPreferencesDao
import net.satius.keyan.infrastructure.user.preferences.UserPreferencesEntity
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

class KeyanDataBaseTypeConverter {

    companion object {
        private const val DIVIDER = "|||||||||"
    }

    @TypeConverter
    fun toEncryptedString(value: String?): EncryptedString? = value?.let {
        val divided = it.split(DIVIDER)
        EncryptedStringImpl(divided[0], Base64.decode(divided[1], Base64.DEFAULT))
    }

    @TypeConverter
    fun fromEncryptedString(value: EncryptedString?): String? = value?.let {
        it.value + DIVIDER + Base64.encodeToString(it.cipheriv, Base64.DEFAULT)
    }

    @TypeConverter
    fun toDisplayTheme(value: String?): DisplayTheme? =
        DisplayTheme.values().firstOrNull { it.name == value }

    @TypeConverter
    fun fromDisplayTheme(value: DisplayTheme?): String? =
        value?.name

    @TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        value ?: return null
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(value),
            ZoneId.of("UTC")
        )
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        value ?: return null
        return value.toEpochSecond(ZoneOffset.UTC)
    }
}

@Database(
    entities = [
        SesameAccountEntity::class,
        SesameDeviceAndroidBootLaunchEntity::class,
        UserPreferencesEntity::class
    ],
    version = 1
)
@TypeConverters(KeyanDataBaseTypeConverter::class)
abstract class KeyanDataBase : RoomDatabase() {
    abstract fun sesameAccountDao(): SesameAccountDao
    abstract fun sesameDeviceAndroidBootLaunchDao(): SesameDeviceAndroidBootLaunchDao
    abstract fun userPreferencesDao(): UserPreferencesDao
}
