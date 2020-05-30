package net.satius.keyan.infrastructure.common

interface CryptoUtil {
    fun decrypt(value: EncryptedString): String
    fun encrypt(value: String): EncryptedString
}
