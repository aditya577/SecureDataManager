package com.example.securedatamanager.ui.documents

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.securedatamanager.data.database.Document

@Composable
fun DocumentItem(
    document: Document,
    onDelete: (Document) -> Unit,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Name: ${document.name}", style = MaterialTheme.typography.bodyLarge)
            Text("Path: ${document.filePath}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onDelete(document) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Document")
                }
                DecryptAndSaveFileWithUserSelection(context, document) // Trigger file save with user-selected path
            }
        }
    }
}



