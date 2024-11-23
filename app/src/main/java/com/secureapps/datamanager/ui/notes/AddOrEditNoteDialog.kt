package com.secureapps.datamanager.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.secureapps.datamanager.data.database.Note
import com.secureapps.datamanager.utils.EncryptionUtil

@Composable
fun AddOrEditNoteDialog(
    noteToEdit: Note?,
    onDismiss: () -> Unit,
    onAddOrEditNote: (Note) -> Unit
) {
    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var content by remember { mutableStateOf(noteToEdit?.let { EncryptionUtil.decrypt(it.content) } ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") }
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
                        if (title.isNotBlank() && content.isNotBlank()) {
                            onAddOrEditNote(
                                Note(
                                    id = noteToEdit?.id ?: 0, // Use existing ID for edit
                                    title = title,
                                    content = EncryptionUtil.encrypt(content),
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                        }
                    }) {
                        Text(if (noteToEdit == null) "Add" else "Save")
                    }
                }
            }
        }
    }
}
