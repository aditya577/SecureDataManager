package com.example.securedatamanager.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object SecurePrefs {
    private const val PREFS_NAME = "secure_prefs"
    private const val PIN_KEY = "user_pin"

    // Lazy initialization to cache EncryptedSharedPreferences
    private var encryptedPrefs: EncryptedSharedPreferences? = null

    private fun getPreferences(context: Context): EncryptedSharedPreferences {
        return encryptedPrefs ?: synchronized(this) {
            try {
                val prefs = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                ) as EncryptedSharedPreferences // Explicit cast
                encryptedPrefs = prefs
                prefs
            } catch (e: Exception) {
                throw RuntimeException("Failed to initialize secure preferences", e)
            }
        }
    }


    fun savePin(context: Context, pin: String) {
        val prefs = getPreferences(context)
        prefs.edit().putString(PIN_KEY, pin).apply()
    }

    fun getPin(context: Context): String? {
        val prefs = getPreferences(context)
        return prefs.getString(PIN_KEY, null)
    }

    fun isPinSet(context: Context): Boolean {
        return getPin(context) != null
    }

    fun clearPin(context: Context) {
        val prefs = getPreferences(context)
        prefs.edit().remove(PIN_KEY).apply()
    }
}