package com.example.securedatamanager.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.securedatamanager.AppNavigator
import com.example.securedatamanager.utils.SecurePrefs

@Composable
fun AuthNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val startDestination = if (SecurePrefs.isPinSet(context)) "authenticate" else "set_pin"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("set_pin") { SetPinScreen(navController) }
        composable("authenticate") { AuthenticateScreen(navController) }
        composable("main") { AppNavigator() }
    }
}
