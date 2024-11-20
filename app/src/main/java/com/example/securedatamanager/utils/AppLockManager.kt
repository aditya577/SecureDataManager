package com.example.securedatamanager.utils

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object AppLockManager : DefaultLifecycleObserver {

    private var backgroundTime: Long = 0
    private const val LOCK_TIMEOUT = 5000L // 5 seconds
    private const val TAG = "AppLockManager"
    var requiresAuthentication: Boolean = false

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // Record the time when the app goes to the background
        backgroundTime = System.currentTimeMillis()
        Log.d(TAG, "App moved to background at: $backgroundTime")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val currentTime = System.currentTimeMillis()
        requiresAuthentication =
            backgroundTime > 0 && (currentTime - backgroundTime) > LOCK_TIMEOUT

        Log.d(TAG, "App moved to foreground at: $currentTime, requiresAuthentication: $requiresAuthentication")
    }

    fun checkAndNavigate(navController: androidx.navigation.NavController) {
        if (requiresAuthentication) {
            navController.navigate("authenticate") {
                popUpTo("main") { inclusive = true }
            }
        }
    }
}
