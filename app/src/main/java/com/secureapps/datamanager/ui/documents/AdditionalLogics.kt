package com.secureapps.datamanager.ui.documents

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.secureapps.datamanager.data.database.Document
import com.secureapps.datamanager.utils.EncryptionUtil
import java.io.File


@Composable
fun DecryptAndSaveFileWithUserSelection(context: Context, document: Document) {
    val decryptedFileName = EncryptionUtil.decrypt(document.name)
    Log.d("DownloadFile", "encrypted file name: ${document.name}")
    Log.d("DownloadFile", "decrypted file name: $decryptedFileName")

    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                var filepath = "${document.folderUUID}/${document.fileUUID}"
                Log.d("DownloadFile", "filepath: $filepath")
                val encryptedFile = File(context.filesDir, filepath)
                Log.d("DownloadFile", "encryptedFile: $encryptedFile")
                val outputStream = context.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    // Decrypt and save the file
                    EncryptionUtil.decryptFile(encryptedFile, outputStream)
                    Toast.makeText(context, "File saved successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to open selected location.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error saving file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "File saving cancelled by user.", Toast.LENGTH_SHORT).show()
        }
    }

    Button(onClick = {
        // Launch save dialog
        saveLauncher.launch(decryptedFileName)
    }) {
        Text("Download File")
    }
}

fun getFilePathFromUri(context: Context, uri: Uri): String? {
    try {
        val fileName = getFileNameFromUri(context, uri)
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, fileName)

        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var name = "temp_file"
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst()) {
            name = it.getString(nameIndex)
        }
    }
    return name
}

