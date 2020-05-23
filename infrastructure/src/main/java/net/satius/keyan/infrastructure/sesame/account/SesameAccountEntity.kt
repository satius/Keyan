package net.satius.keyan.infrastructure.sesame.account

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.satius.keyan.infrastructure.common.EncryptedString

@Entity
data class SesameAccountEntity (
    var name: String,
    val authToken: EncryptedString
) {
    @PrimaryKey(autoGenerate = true)
    var accountId: Long = 0
}
