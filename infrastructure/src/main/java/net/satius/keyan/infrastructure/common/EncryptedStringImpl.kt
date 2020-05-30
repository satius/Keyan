package net.satius.keyan.infrastructure.common

data class EncryptedStringImpl(
    override val value: String,
    override val cipheriv: ByteArray
) : EncryptedString {
    override fun equals(other: Any?) = (other as? EncryptedString)?.let {
        (value == it.value) && (cipheriv.contentEquals(it.cipheriv))
    } ?: false

    override fun hashCode() = super.hashCode()
}
