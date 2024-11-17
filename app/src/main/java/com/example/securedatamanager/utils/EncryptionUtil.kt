package com.example.securedatamanager.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private const val SECRET_KEY = "1234567890123456" // Replace with a securely generated key

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val key = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedValue = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        return try {
            if (!isBase64(data)) {
                return data // Return as-is if it's not Base64-encoded (plaintext)
            }
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val key = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val decodedValue = Base64.decode(data, Base64.DEFAULT)
            val decryptedValue = cipher.doFinal(decodedValue)
            String(decryptedValue)
        } catch (e: Exception) {
            data // Return the original data in case of any exception
        }
    }

    fun isBase64(data: String): Boolean {
        return try {
            Base64.decode(data, Base64.DEFAULT)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}

