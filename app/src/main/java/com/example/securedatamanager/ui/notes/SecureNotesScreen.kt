package com.example.securedatamanager.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.securedatamanager.data.database.Note

@Composable
fun SecureNotesScreen(viewModel: NoteViewModel) {
    val notes by viewModel.notes.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) } // Track the note being edited

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
                noteToEdit = null // Set to null for adding a new note
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onDelete = { viewModel.deleteNote(it) },
                        onEdit = {
                            noteToEdit = it // Set the note for editing
                            showDialog = true
                        }
                    )
                }
            }
        }
    )

    if (showDialog) {
        AddOrEditNoteDialog(
            noteToEdit = noteToEdit, // Pass the note to edit, or null for adding a new note
            onDismiss = { showDialog = false },
            onAddOrEditNote = { newNote ->
                if (noteToEdit == null) {
                    viewModel.addNote(newNote) // Add new note
                } else {
                    viewModel.addNote(newNote.copy(id = noteToEdit!!.id)) // Update existing note
                }
                showDialog = false
            }
        )
    }
}
