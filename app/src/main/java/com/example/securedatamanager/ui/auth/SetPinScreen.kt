package com.example.securedatamanager.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.securedatamanager.utils.SecurePrefs

@Composable
fun SetPinScreen(navController: NavController) {
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = pin,
            onValueChange = { pin = it },
            label = { Text("Enter PIN") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = confirmPin,
            onValueChange = { confirmPin = it },
            label = { Text("Confirm PIN") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (pin == confirmPin) {
                    SecurePrefs.savePin(context, pin)
                    navController.navigate("authenticate") {
                        popUpTo("set_pin") { inclusive = true }
                    }
                } else {
                    errorMessage = "PINs do not match!"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set PIN")
        }
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}
