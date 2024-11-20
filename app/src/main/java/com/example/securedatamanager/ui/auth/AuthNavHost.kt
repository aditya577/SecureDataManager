package com.example.securedatamanager.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.securedatamanager.ui.commons.AppNavigator
import com.example.securedatamanager.utils.AppLockManager
import com.example.securedatamanager.utils.SecurePrefs

@Composable
fun AuthNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Check if PIN is set
    val isPinSet = SecurePrefs.isPinSet(context)

    // Determine start destination based on PIN state and authentication requirement
    val startDestination = when {
        !isPinSet -> "set_pin"
        else -> "authenticate"
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe lifecycle and check for authentication
    DisposableEffect(lifecycleOwner) {
        val observer = object : androidx.lifecycle.DefaultLifecycleObserver {
            override fun onStart(owner: androidx.lifecycle.LifecycleOwner) {
                super.onStart(owner)
                AppLockManager.checkAndNavigate(navController)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("set_pin") { SetPinScreen(navController) }
        composable("authenticate") { AuthenticateScreen(navController) }
        composable("main") { AppNavigator() }
    }
}
