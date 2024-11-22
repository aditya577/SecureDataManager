package com.secureapps.datamanager.ui.documents

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secureapps.datamanager.utils.EncryptionUtil

@Composable
fun DocumentItem(
    document: com.secureapps.datamanager.data.database.Document,
    onDelete: (com.secureapps.datamanager.data.database.Document) -> Unit,
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
            val decryptedName = EncryptionUtil.decrypt(document.name)
            Text("Name: $decryptedName", style = MaterialTheme.typography.bodyLarge)
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



