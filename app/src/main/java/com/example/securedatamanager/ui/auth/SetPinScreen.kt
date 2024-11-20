package com.example.securedatamanager.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.securedatamanager.utils.SecurePrefs

@Composable
fun SetPinScreen(navController: NavController) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val minPinLength = 4 // Set your desired minimum length

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Set Your PIN",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Enter PIN Field
        TextField(
            value = pin,
            onValueChange = { pin = it }, // Allow any characters
            label = { Text("Enter PIN") },
            visualTransformation = PasswordVisualTransformation(), // Obscure input
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm PIN Field
        TextField(
            value = confirmPin,
            onValueChange = { confirmPin = it }, // Allow any characters
            label = { Text("Confirm PIN") },
            visualTransformation = PasswordVisualTransformation(), // Obscure input
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Set PIN Button
        Button(
            onClick = {
                when {
                    pin.isBlank() || confirmPin.isBlank() -> {
                        errorMessage = "PIN cannot be empty!"
                        pin = ""
                        confirmPin = ""
                    }
                    pin != confirmPin -> {
                        errorMessage = "PINs do not match!"
                        pin = ""
                        confirmPin = ""
                    }
                    pin.length < minPinLength -> {
                        errorMessage = "PIN must be at least $minPinLength characters long!"
                        pin = ""
                        confirmPin = ""
                    }
                    else -> {
                        SecurePrefs.savePin(context, pin)
                        navController.navigate("authenticate") {
                            popUpTo("set_pin") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set PIN")
        }

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
