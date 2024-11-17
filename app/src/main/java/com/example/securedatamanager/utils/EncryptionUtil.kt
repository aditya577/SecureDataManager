package com.example.securedatamanager.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private const val SECRET_KEY = "1234567890123456" // Use a securely generated key in production

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val key = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedValue = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val key = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decodedValue = Base64.decode(data, Base64.DEFAULT)
        val decryptedValue = cipher.doFinal(decodedValue)
        return String(decryptedValue)
    }
}
