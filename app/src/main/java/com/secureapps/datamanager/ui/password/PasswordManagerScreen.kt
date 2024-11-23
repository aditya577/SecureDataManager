package com.secureapps.datamanager.ui.password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.secureapps.datamanager.data.database.Password
import com.secureapps.datamanager.ui.commons.AppBarWithBackButton

@Composable
fun PasswordManagerScreen(
    viewModel: PasswordManagerViewModel = viewModel(),
    onBackClick: (() -> Unit)? = null
) {
    val passwords by viewModel.passwords.collectAsState()
    var showDialog by remember { mutableStateOf(false)}
    var passwordToEdit by remember { mutableStateOf<Password?>(null) }
    var passwordToDelete by remember { mutableStateOf<Password?>(null) }

    Scaffold(
        topBar = {
            AppBarWithBackButton(
                title = "Password Manager",
                showBackButton = onBackClick != null, // Show back button only if onBackClick is provided
                onBackClick = { onBackClick?.invoke() } // Invoke back click if not null
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
                passwordToEdit = null
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Password")
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .imePadding(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(passwords) { password ->
                    PasswordItem(
                        password = password,
                        onEdit = {
                            passwordToEdit = it
                            showDialog = true
                        },
                        onDelete = { passwordToDelete = password }
                    )
                }
            }
        }
    )

    if (showDialog) {
        AddPasswordDialog(
            passwordToEdit = passwordToEdit,
            onDismiss = { showDialog = false },
            onAddPassword = { newPassword ->
                if(passwordToEdit == null) {
                    viewModel.addPassword(newPassword)
                } else {
                    viewModel.addPassword(newPassword.copy(id = passwordToEdit!!.id))
                }
                showDialog = false
            }
        )
    }

    if (passwordToDelete != null) {
        AlertDialog(
            onDismissRequest = { passwordToDelete = null },
            title = { Text(text = "Delete Password") },
            text = { Text(text = "Are you sure you want to delete this password? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    passwordToDelete?.let {
                        // Delete the note from the database
                        viewModel.deletePassword(it)
                    }
                    passwordToDelete = null // Dismiss the dialog
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { passwordToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
