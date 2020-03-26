package net.satius.keyan.infrastructure.common

interface EncryptedString {
    val value: String
    val cipheriv: ByteArray
}
