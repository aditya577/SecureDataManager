package com.example.securedatamanager.ui.documents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun DocumentStorageScreen(viewModel: DocumentViewModel) {
    val documents by viewModel.documents.collectAsState()
    var showUploadDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showUploadDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Upload Document")
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(documents) { document ->
                    DocumentItem(
                        document = document,
                        onDelete = { viewModel.deleteDocument(it) },
                        context = context
                    )
                }
            }
        }
    )

    if (showUploadDialog) {
        UploadDocumentDialog(
            onDismiss = { showUploadDialog = false },
            onUpload = { newDocument ->
                viewModel.addDocument(newDocument)
                showUploadDialog = false
            }
        )
    }
}

