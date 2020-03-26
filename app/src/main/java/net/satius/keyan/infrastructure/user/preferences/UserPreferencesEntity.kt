package net.satius.keyan.infrastructure.user.preferences

import androidx.room.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

private const val UPDATE_AT_COLUMN_NAME = "updateAt"

@Entity(
    indices = [Index(
        value = [UPDATE_AT_COLUMN_NAME],
        unique = true
    )]
)
data class UserPreferencesEntity(
    var displayTheme: DisplayTheme,
    @ColumnInfo(name = UPDATE_AT_COLUMN_NAME)
    var updateAt: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

enum class DisplayTheme {
    LIGHT,
    DARK,
    SYSTEM_DEFAULT
}
