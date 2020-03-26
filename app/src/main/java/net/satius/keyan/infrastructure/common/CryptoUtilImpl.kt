package net.satius.keyan.infrastructure.common

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoUtilImpl(val context: Context) : CryptoUtil {

    companion object {
        private const val ALIAS = "CryptoUtilImpl"
    }

    private val aesCrypto = AesCrypto(ALIAS)

    override fun decrypt(value: EncryptedString): String {
        val bytes = Base64.decode(value.value, Base64.DEFAULT)
        val result = aesCrypto.decrypt(bytes, value.cipheriv)
        return String(result.bytes)
    }

    override fun encrypt(value: String): EncryptedString {
        val result = aesCrypto.encrypt(value.toByteArray())
        return EncryptedStringImpl(
            Base64.encodeToString(result.bytes, Base64.DEFAULT),
            result.cipherIV
        )
    }
}


class AesCrypto(private val alias: String) {

    companion object {
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7

        private const val KEY_STORE_INSTANCE_TYPE = "AndroidKeyStore"
    }

    private val cipher =
        Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + BLOCK_MODE + "/" + ENCRYPTION_PADDING)

    private val keyStore = KeyStore.getInstance(KEY_STORE_INSTANCE_TYPE).apply { load(null) }

    init {
        createKeystoreAliasIfNeeded()
    }

    private fun createKeystoreAliasIfNeeded() {
        if (!keyStore.containsAlias(alias)) this.createNewKey()
    }

    fun encrypt(plainByte: ByteArray): EncryptResult {
        createKeystoreAliasIfNeeded()
        val encryptKey = getEncryptKey()
        cipher.init(Cipher.ENCRYPT_MODE, encryptKey)
        return EncryptResult(cipher.doFinal(plainByte), cipher.iv)
    }

    fun decrypt(encryptedByte: ByteArray, cipherIV: ByteArray?): DecryptResult {
        createKeystoreAliasIfNeeded()

        cipher.init(Cipher.DECRYPT_MODE, getDecryptKey(), cipherIV?.let { IvParameterSpec(it) })
        return DecryptResult(
            cipher.doFinal(encryptedByte),
            cipher.iv
        )
    }

    fun reset(): Boolean {
        val hasAlias = keyStore.containsAlias(alias)
        if (hasAlias) {
            keyStore.deleteEntry(alias)
        }
        return hasAlias
    }

    private fun getEncryptKey() = keyStore.getKey(alias, null) as SecretKey

    private fun getDecryptKey() = keyStore.getKey(alias, null) as SecretKey

    private fun createNewKey() {
        KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEY_STORE_INSTANCE_TYPE
        ).apply {
            init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(ENCRYPTION_PADDING)
                    .build()
            )
            generateKey()
        }

    }

    class EncryptResult(val bytes: ByteArray, var cipherIV: ByteArray)

    class DecryptResult(val bytes: ByteArray, var cipherIV: ByteArray?)
}