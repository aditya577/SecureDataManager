package com.secureapps.datamanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.secureapps.datamanager.ui.auth.AuthNavHost
import com.secureapps.datamanager.ui.theme.SecureDataManagerTheme
import com.secureapps.datamanager.utils.AppLockManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add AppLockManager as a lifecycle observer
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_STOP -> AppLockManager.onStop(ProcessLifecycleOwner.get())
                    Lifecycle.Event.ON_START -> AppLockManager.onStart(ProcessLifecycleOwner.get())
                    else -> Unit
                }
            }
        )

        // Set content for the app
        setContent {
            SecureDataManagerTheme {
                AuthNavHost()
            }
        }
    }
}
