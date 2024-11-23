package com.secureapps.datamanager.ui.documents


import android.net.Uri
import android.util.Log
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
import java.util.UUID

@Composable
fun UploadDocumentDialog(
    onDismiss: () -> Unit,
    onUpload: (Document) -> Unit
) {
    var fileName by remember { mutableStateOf("") }
    var filePath by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf("") }
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
                    onValueChange = {
                        fileName = it
                        errorMessage = "" // Clear any previous error when the name is updated
                    },
                    label = { Text("Document Name (Optional)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    launcher.launch(arrayOf("*/*")) // Trigger the file picker
                }) {
                    Text("Choose File")
                }

                // Show selected file name if a file is picked
                filePath?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selected File: ${File(it).name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
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
                            val originalExtension = originalFile.extension
                            val enteredExtension = File(fileName).extension

                            // Determine the corrected file name
                            val correctedFileName = if (fileName.isBlank()) {
                                originalFile.name
                            } else if (enteredExtension.isEmpty()) {
                                "$fileName.$originalExtension"
                            } else if (enteredExtension != originalExtension) {
                                errorMessage = "Please use the correct extension: .$originalExtension"
                                return@TextButton
                            } else {
                                fileName
                            }

                            // Generate a unique alphanumeric identifier
                            val fileUUID = UUID.randomUUID().toString().replace("-", "")
                            val folderUUID = UUID.randomUUID().toString().replace("-", "")

                            // Encrypt the file name
                            Log.d("UploadFile", "filename: $correctedFileName")
                            val encryptedFileName = EncryptionUtil.encrypt(correctedFileName)
                            Log.d("UploadFile", "encryptedFileName: $encryptedFileName")

                            // Create a sub-folder with a random name
                            val subFolder = File(context.filesDir, folderUUID)
                            if (!subFolder.exists()) subFolder.mkdir()

                            val encryptedFile = File(subFolder, fileUUID)

                            try {
                                // Encrypt the file
                                EncryptionUtil.encryptFile(originalFile, encryptedFile)

                                if (originalFile.exists()) {
                                    originalFile.delete()
                                }

                                // Trigger the upload callback with metadata
                                onUpload(
                                    Document(
                                        name = encryptedFileName,
                                        folderUUID = folderUUID,
                                        fileUUID = fileUUID,
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                                onDismiss()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to encrypt file: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }) {
                        Text("Upload")
                    }
                }

                // Show error message if there's any
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

