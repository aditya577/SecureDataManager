package com.secureapps.datamanager.ui.password

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PasswordItem(password: com.secureapps.datamanager.data.database.Password, onDelete: (com.secureapps.datamanager.data.database.Password) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Site: ${password.siteName}", style = MaterialTheme.typography.bodyLarge)
            Text("Username: ${password.username}", style = MaterialTheme.typography.bodyMedium)
            Text("Password: ${password.password}", style = MaterialTheme.typography.bodySmall)
            IconButton(onClick = { onDelete(password) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Password")
            }
        }
    }
}
