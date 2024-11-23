package com.secureapps.datamanager.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object EncryptionUtil {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "SecureDataManagerKey"

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return if (keyStore.containsAlias(KEY_ALIAS)) {
            val key = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
            key ?: throw IllegalStateException("Key is invalid or null!")
        } else {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        val iv = cipher.iv
        val encryptedValue = cipher.doFinal(data.toByteArray())
        val combined = iv + encryptedValue
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        return try {
            val decodedData = Base64.decode(data, Base64.DEFAULT)
            val iv = decodedData.copyOfRange(0, 12)
            val encryptedValue = decodedData.copyOfRange(12, decodedData.size)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))

            val decryptedValue = cipher.doFinal(encryptedValue)
            String(decryptedValue)
        } catch (e: Exception) {
            Log.e("EncryptionUtil", "Decryption failed: ${e.message}")
            e.printStackTrace()
            data // Return the original input in case of an error
        }
    }

    fun encryptFile(inputFile: File, outputFile: File) {
        require(inputFile.exists()) { "Input file does not exist!" }
        require(inputFile.length() > 0) { "Input file is empty!" }

        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val secretKey = getSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val iv = cipher.iv

            FileOutputStream(outputFile).use { fos ->
                fos.write(iv)

                FileInputStream(inputFile).use { fis ->
                    CipherOutputStream(fos, cipher).use { cos ->
                        fis.copyTo(cos)
                    }
                }
            }

            Log.d("EncryptionUtil", "File encrypted successfully!")
        } catch (e: Exception) {
            Log.e("EncryptionUtil", "Error during encryption: ${e.message}")
            throw e
        }
    }

    fun decryptFile(inputFile: File, outputStream: OutputStream) {
        require(inputFile.exists()) { "Input file does not exist!" }

        try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")

            FileInputStream(inputFile).use { fis ->
                val iv = ByteArray(12)
                fis.read(iv)

                val secretKey = getSecretKey()
                cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

                CipherInputStream(fis, cipher).use { cis ->
                    cis.copyTo(outputStream)
                }
            }

            Log.d("EncryptionUtil", "Decryption completed successfully!")
        } catch (e: Exception) {
            Log.e("EncryptionUtil", "Error during decryption: ${e.message}")
            throw e
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

