package com.example.securedatamanager.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.securedatamanager.ui.commons.AppBarWithBackButton
import com.example.securedatamanager.utils.SecurePrefs

@Composable
fun AuthenticateScreen(navController: NavController) {
    var pin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppBarWithBackButton(
                title = "Authenticate",
                showBackButton = false,
                onBackClick = { navController.popBackStack() }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .imePadding(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Enter PIN",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextField(
                    value = pin,
                    onValueChange = { pin = it },
                    label = { Text("Enter PIN") },
                    visualTransformation = PasswordVisualTransformation(), // Obscure input
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val storedPin = SecurePrefs.getPin(context) // Use the context safely here
                        if (pin == storedPin) {
                            navController.navigate("main") {
                                popUpTo("authenticate") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Invalid PIN!"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Authenticate")
                }
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    )
}

