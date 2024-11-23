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
import com.example.securedatamanager.ui.commons.AppBarWithBackButton

@Composable
fun PasswordManagerScreen(
    viewModel: PasswordManagerViewModel = viewModel(),
    onBackClick: (() -> Unit)? = null
) {
    val passwords by viewModel.passwords.collectAsState()
    var showDialog by remember { mutableStateOf(false)}

    Scaffold(
        topBar = {
            AppBarWithBackButton(
                title = "Password Manager",
                showBackButton = onBackClick != null, // Show back button only if onBackClick is provided
                onBackClick = { onBackClick?.invoke() } // Invoke back click if not null
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
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
                    PasswordItem(password, onDelete = { viewModel.deletePassword(it) })
                }
            }
        }
    )

    if (showDialog) {
        AddPasswordDialog(
            onDismiss = { showDialog = false },
            onAddPassword = { newPassword ->
                viewModel.addPassword(newPassword)
                showDialog = false
            }
        )
    }
}
