package com.secureapps.datamanager.ui.documents

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.secureapps.datamanager.data.database.Document
import com.secureapps.datamanager.utils.EncryptionUtil
import java.io.File

@Composable
fun UploadDocumentDialog(
    onDismiss: () -> Unit,
    onUpload: (Document) -> Unit
) {
    var fileName by remember { mutableStateOf("") }
    var filePath by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Define the file picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val pickedFilePath = getFilePathFromUri(context, it)
            filePath = pickedFilePath
        }
    }

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
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Document Name (Optional)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    launcher.launch(arrayOf("*/*")) // Trigger the file picker
                }) {
                    Text("Choose File")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        if (filePath != null) {
                            val originalFile = File(filePath!!)
                            val encryptedFile = File(
                                context.filesDir,
                                "${fileName.ifBlank { originalFile.name }}.enc"
                            )
                            try {
                                // Encrypt the file
                                EncryptionUtil.encryptFile(originalFile, encryptedFile)

                                if (originalFile.exists()) {
                                    originalFile.delete()
                                }

                                // Trigger the upload callback with metadata
                                onUpload(
                                    Document(
                                        name = fileName.ifBlank { originalFile.name },
                                        filePath = encryptedFile.absolutePath,
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to encrypt file: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }) {
                        Text("Upload")
                    }
                }
            }
        }
    }
}

