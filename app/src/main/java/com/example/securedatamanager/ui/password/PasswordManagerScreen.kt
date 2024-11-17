package com.example.securedatamanager.ui.password

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PasswordManagerScreen(
    viewModel: PasswordManagerViewModel = viewModel() // Pass the ViewModel
) {
    val passwords by viewModel.passwords.collectAsState() // Collect passwords as state
    var showDialog by remember { mutableStateOf(false) } // Dialog visibility state

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Password")
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(passwords) { password ->
                    PasswordItem(password, onDelete = { viewModel.deletePassword(it) })
                }
            }
        }
    )

    if (showDialog) {
        AddPasswordDialog(
            onDismiss = { showDialog = false },
            onAddPassword = { newPassword ->
                viewModel.addPassword(newPassword) // Add password to ViewModel
                showDialog = false
            }
        )
    }
}
