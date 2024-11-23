package com.secureapps.datamanager.ui.password

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import com.secureapps.datamanager.data.database.Password
import com.secureapps.datamanager.utils.EncryptionUtil

@Composable
fun AddPasswordDialog(
    onDismiss: () -> Unit,
    onAddPassword: (Password) -> Unit
) {
    var siteName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = siteName,
                    onValueChange = { siteName = it },
                    label = { Text("Site Name") }
                )
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") }
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        if (siteName.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
                            onAddPassword(
                                Password(
                                    siteName = siteName,
                                    username = username,
                                    password = EncryptionUtil.encrypt(password)
                                )
                            )
                            onDismiss()
                        }
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
